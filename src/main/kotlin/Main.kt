import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import entity.Player
import entity.Skill
import entity.UserData
import factory.TourSeatingFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import login.LoginManager
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    println(Gson().toJson(Player("strelas", "strelas", Skill.A, arrayListOf())))
    val loginManager = LoginManager()
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    install(DefaultHeaders) {
        header(HttpHeaders.AccessControlAllowOrigin, "Access-Control-Allow-Origin, Accept")
        header(HttpHeaders.ETag, "7c876b7e")
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
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
        post("/login") {
            val json = Gson().fromJson<HashMap<String, Any>>(call.receive<String>(), java.util.HashMap::class.java)
            if (loginManager.login(json["login"].toString(), json["password"].toString())) {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", json["login"].toString())
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                    .sign(Algorithm.HMAC256(secret))
                call.respondText(hashMapOf("token" to token).toString())
            } else {
                call.respondText { hashMapOf("token" to "").toString() }
            }
        }
        authenticate("auth-jwt") {
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
                call.respondText { TourSeatingFactory(players.toList(), 6).generate().toString() }
            }
        }
    }
}