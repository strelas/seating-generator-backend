import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import entity.Player
import entity.Skill
import entity.Tournament
import factory.TourSeatingFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*
import kotlin.random.Random
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}

@ExperimentalTime
fun Application.module(testing: Boolean = false) {
    val appAssembly = AppAssembly()

    val loginManager = appAssembly.loginManager
    val tournamentsRepository = appAssembly.tournamentsRepository
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    install(CORS) {
        anyHost()
    }
    install(DefaultHeaders) {
        header(HttpHeaders.AccessControlAllowOrigin, "*")
        header(HttpHeaders.AccessControlAllowMethods, "DELETE, POST, GET, OPTIONS, PUT")
        header(
            HttpHeaders.AccessControlAllowHeaders,
            "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"
        )
        header(HttpHeaders.ETag, "7c876b7e")
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->

                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
    routing {
        options {
            call.respondText("", status = HttpStatusCode.NoContent)
        }
        post("api/login") {
            val json = Gson().fromJson<HashMap<String, Any>>(call.receive<String>(), java.util.HashMap::class.java)
            if (loginManager.login(json["login"].toString(), json["password"].toString())) {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", json["login"].toString())
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                    .sign(Algorithm.HMAC256(secret))
                call.respondText(Gson().toJson(hashMapOf("token" to token)), status = HttpStatusCode.OK)
            } else {
                call.respondText(Gson().toJson(hashMapOf("token" to "")), status = HttpStatusCode.OK)
            }
        }
        authenticate("auth-jwt") {
            post("/api/tournaments/{id}/append_players") {
                val id = call.parameters["id"]!!.toInt()
                println(id)
                val type = object : TypeToken<List<Player>>() {}.type
                val players = Gson().fromJson<List<Player>>(call.receive<String>(), type)
                val repository = appAssembly.getTournamentRepository(id)
                println(id)
                println(players)
                for (player in players) {
                    repository.appendPlayer(player)
                }
                call.respond(HttpStatusCode.OK)
            }
            post("/api/tournaments/{id}/delete_players") {
                val id = call.parameters["id"]!!.toInt()
                val type = object : TypeToken<List<Player>>() {}.type
                val players = Gson().fromJson<List<Player>>(call.receive<String>(), type)
                val repository = appAssembly.getTournamentRepository(id)
                for (player in players) {
                    repository.removePlayer(player)
                }
                call.respond(HttpStatusCode.OK)
            }
            post("/api/tournaments") {
                val nickname = call.receive<String>()

                val tournaments = loginManager.getTournamentsIds(nickname)
                    .map { tournamentsRepository.getTournamentById(it) }
                    .filter { loginManager.ownerOf(nickname, it.id) }

                call.respondText(Gson().toJson(tournaments), status = HttpStatusCode.OK)
            }
            get("/api/tournaments/{id}/players") {
                val id = call.parameters["id"]!!.toInt()
                val repository = appAssembly.getTournamentRepository(id)

                val nickname = call.principal<UserIdPrincipal>()?.name
                if (nickname == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                if (!loginManager.ownerOf(nickname, id)) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val players = repository.getPlayers()
                call.respondText(Gson().toJson(players), status = HttpStatusCode.OK)
            }
            get("/api/check") {
                call.respond(HttpStatusCode.OK)
            }
        }
        get("/") {
            val players = Array(60) {
                val skill = when (Random.nextInt() % 3) {
                    0 -> Skill.A
                    1 -> Skill.B
                    2 -> Skill.C
                    else -> Skill.B
                }
                val cannotMeet = arrayListOf<String>()
                if (it == 0) {
                    cannotMeet.add("01")
                }
                if (it == 1) {
                    cannotMeet.add("00")
                }
                if (it == 2) {
                    cannotMeet.add("03")
                }
                if (it == 3) {
                    cannotMeet.add("02")
                }
                if (it == 4) {
                    cannotMeet.add("05")
                }
                if (it == 5) {
                    cannotMeet.add("04")
                }
                if (it == 6) {
                    cannotMeet.add("1")
                }
                Player("0$it", "0$it", skill, cannotMeet)
            }
            call.respondText(
                TourSeatingFactory(players.toList(), 6).generate().toString(),
                status = HttpStatusCode.OK
            )
        }
    }
}