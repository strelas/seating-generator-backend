package factory

import entity.Player
import entity.Skill
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TourSeatingFactoryTest {
    @Test
    fun testGenerate() {
        val players = Array(30) {
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
                cannotMeet.add("07")
                cannotMeet.add("08")
            }
            if (it == 7) {
                cannotMeet.add("06")
            }
            if (it == 8) {
                cannotMeet.add("06")
            }
            if (it == 9) {
                cannotMeet.add("0")
                cannotMeet.add("1")
                cannotMeet.add("2")
            }
            Player("0$it", "0$it", skill, cannotMeet)
        }.toList()
        var flag = true

        repeat(10) {
            val seating = TourSeatingFactory.generate(players, 6)
            if (seating.rounds.any { !it.isLegalRound }) {
                flag = false
            }
        }
        assertEquals(true, flag)
    }
}