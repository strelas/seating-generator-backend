package factory

import entity.*
import java.lang.IllegalArgumentException

class TourSeatingFactory(private val players: List<Player>, private val roundCount: Int) {
    init {
        if (players.size % 10 != 0) {
            throw IllegalArgumentException("players count should be divided by 10")
        }
        if (players.isEmpty()) {
            throw IllegalArgumentException("players count should be bigger then zero")
        }
    }

    fun generate(): TourSeating {
        return generateSomeLegalSeating()
    }

    private fun generateSomeLegalSeating(): TourSeating {
        return TourSeating(Array(roundCount) { generateSomeLegalRound() }.toMutableList())
    }

    private fun generateSomeLegalRound(): RoundSeating {
        val someRound = generateSomeIllegalRound()
        for (table in someRound.tables) {
            for (player in table.players) {
                if (!table.playerCanSitHere(player, player)) {
                    var flag = false
                    for (anotherTable in someRound.tables) {
                        if (flag) {
                            break
                        }
                        if (table == anotherTable) {
                            continue
                        }

                        for (anotherPlayer in anotherTable.players) {
                            if (anotherPlayer.skill != player.skill) {
                                continue
                            }

                            if (table.playerCanSitHere(anotherPlayer, player)
                                && anotherTable.playerCanSitHere(player, anotherPlayer)
                            ) {
                                flag = true
                                anotherTable.players[anotherTable.players.indexOf(anotherPlayer)] = player
                                table.players[table.players.indexOf(player)] = anotherPlayer
                                break
                            }
                        }
                    }
                }
            }
        }
        return someRound
    }

    private fun generateSomeIllegalRound(): RoundSeating {
        val A = players.filter { it.skill == Skill.A }.shuffled()
        val B = players.filter { it.skill == Skill.B }.shuffled()
        val C = players.filter { it.skill == Skill.C }.shuffled()
        val tables = Array(players.size / 10) {
            Array(10) { Player("", "", Skill.A, arrayListOf()) }
        }
        A.forEachIndexed { index, player ->
            tables[index % tables.size][index / tables.size] = player
        }
        B.forEachIndexed { index, player ->
            tables[(index + A.size) % tables.size][(index + A.size) / tables.size] = player
        }
        C.forEachIndexed { index, player ->
            tables[(index + A.size + B.size) % tables.size][(index + A.size + B.size) / tables.size] = player
        }
        return RoundSeating(tables.mapIndexed { index, arrayOfPlayers ->
            TableSeating(
                arrayOfPlayers.toList().shuffled().toMutableList(), "${index + 1}"
            )
        }.toMutableList())
    }
}