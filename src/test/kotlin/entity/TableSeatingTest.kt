package entity

import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TableSeatingTest {
    @Test
    fun testCanSitHere() {
        val table = TableSeating(arrayListOf(
            Player("1", "1", Skill.A, arrayListOf("Angry referee")),
            Player("2", "2", Skill.A, arrayListOf("11")),
            Player("3", "3", Skill.A, arrayListOf()),
            Player("4", "4", Skill.A, arrayListOf()),
            Player("5", "5", Skill.A, arrayListOf()),
            Player("6", "6", Skill.A, arrayListOf("Refere")),
            Player("7", "7", Skill.A, arrayListOf()),
            Player("8", "8", Skill.A, arrayListOf()),
            Player("9", "9", Skill.A, arrayListOf()),
            Player("10", "10", Skill.A, arrayListOf()),
        ), "Referee")

        val newPlayer = Player("11", "11", Skill.A, arrayListOf("2"))
        assertEquals(true, table.playerCanSitHere(newPlayer, table.players[1]))
        assertEquals(false, table.playerCanSitHere(newPlayer, table.players[0]))

        val anotherPlayer = Player("12", "12", Skill.A, arrayListOf("Referee"))
        assertEquals(false, table.playerCanSitHere(anotherPlayer, table.players[0]))
    }

    @Test
    fun testIsLegalTable() {
        val correctTable = TableSeating(arrayListOf(
            Player("1", "1", Skill.A, arrayListOf("Angry referee")),
            Player("2", "2", Skill.A, arrayListOf("11")),
            Player("3", "3", Skill.A, arrayListOf()),
            Player("4", "4", Skill.A, arrayListOf()),
            Player("5", "5", Skill.A, arrayListOf()),
            Player("6", "6", Skill.A, arrayListOf("Refere")),
            Player("7", "7", Skill.A, arrayListOf()),
            Player("8", "8", Skill.A, arrayListOf()),
            Player("9", "9", Skill.A, arrayListOf()),
            Player("10", "10", Skill.A, arrayListOf()),
        ), "Referee")
        assertEquals(true, correctTable.isLegalTable)

        val incorrectTableWithPlayers = TableSeating(arrayListOf(
            Player("1", "1", Skill.A, arrayListOf("Angry referee")),
            Player("2", "2", Skill.A, arrayListOf("10")),
            Player("3", "3", Skill.A, arrayListOf()),
            Player("4", "4", Skill.A, arrayListOf()),
            Player("5", "5", Skill.A, arrayListOf()),
            Player("6", "6", Skill.A, arrayListOf("Refere")),
            Player("7", "7", Skill.A, arrayListOf()),
            Player("8", "8", Skill.A, arrayListOf()),
            Player("9", "9", Skill.A, arrayListOf()),
            Player("10", "10", Skill.A, arrayListOf()),
        ), "Referee")
        assertEquals(false, incorrectTableWithPlayers.isLegalTable)

        val incorrectTableWithReferee = TableSeating(arrayListOf(
            Player("1", "1", Skill.A, arrayListOf("Angry referee")),
            Player("2", "2", Skill.A, arrayListOf("11")),
            Player("3", "3", Skill.A, arrayListOf()),
            Player("4", "4", Skill.A, arrayListOf()),
            Player("5", "5", Skill.A, arrayListOf()),
            Player("6", "6", Skill.A, arrayListOf("Referee")),
            Player("7", "7", Skill.A, arrayListOf()),
            Player("8", "8", Skill.A, arrayListOf()),
            Player("9", "9", Skill.A, arrayListOf()),
            Player("10", "10", Skill.A, arrayListOf()),
        ), "Referee")
        assertEquals(false, incorrectTableWithReferee.isLegalTable)
    }
}