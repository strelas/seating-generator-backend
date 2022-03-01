package entity

import org.junit.Assert.assertEquals
import org.junit.Test


internal class RoundSeatingTest {
    @Test
    fun testCorrectRound() {
        val round = RoundSeating(Array(3) { roundIndex ->
            val players = Array(10) { playerIndex ->
                val id = roundIndex*10+playerIndex+1
                val cannotMeet = arrayListOf<String>()
                if (id == 1) {
                    cannotMeet.add("011")
                }
                if (id == 11) {
                    cannotMeet.add("01")
                }
                if (id == 2) {
                    cannotMeet.add("030")
                }
                if (id == 30) {
                    cannotMeet.add("02")
                }
                Player("0$id", "0$id", Skill.A, cannotMeet)
            }.toMutableList()
            TableSeating(players, "$roundIndex")
        }.toMutableList())

        assertEquals(true, round.isLegalRound)
    }

    @Test
    fun testIncorrectRoundWithPlayers() {
        val round = RoundSeating(Array(3) { roundIndex ->
            val players = Array(10) { playerIndex ->
                val id = roundIndex*10+playerIndex+1
                val cannotMeet = arrayListOf<String>()
                if (id == 1) {
                    cannotMeet.add("010")
                }
                if (id == 10) {
                    cannotMeet.add("01")
                }
                if (id == 2) {
                    cannotMeet.add("030")
                }
                if (id == 30) {
                    cannotMeet.add("02")
                }
                Player("0$id", "0$id", Skill.A, cannotMeet)
            }.toMutableList()
            TableSeating(players, "$roundIndex")
        }.toMutableList())

        assertEquals(false, round.isLegalRound)
    }

    @Test
    fun testIncorrectRoundWithReferee() {
        val round = RoundSeating(Array(3) { roundIndex ->
            val players = Array(10) { playerIndex ->
                val id = roundIndex*10+playerIndex+1
                val cannotMeet = arrayListOf<String>()
                if (id == 1) {
                    cannotMeet.add("Angry referee 0")
                }
                if (id == 2) {
                    cannotMeet.add("030")
                }
                if (id == 30) {
                    cannotMeet.add("02")
                }
                Player("0$id", "0$id", Skill.A, cannotMeet)
            }.toMutableList()
            TableSeating(players, "Angry referee $roundIndex")
        }.toMutableList())
        assertEquals(false, round.isLegalRound)
    }
}