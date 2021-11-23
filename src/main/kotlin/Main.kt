import entity.Player
import entity.Skill
import factory.TourSeatingFactory
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.random.Random

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                val players = Array(60) {
                    val skill = when (Random.nextInt()%3) {
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
    }.start(wait = true)
}