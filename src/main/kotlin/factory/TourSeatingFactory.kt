package factory

import entity.*
import java.lang.IllegalArgumentException

class TourSeatingFactory {
    companion object {
        fun generate(players: List<Player>, roundCount: Int): TourSeating {
            if (players.size % 10 != 0) {
                throw IllegalArgumentException("players count should be divided by 10")
            }
            if (players.isEmpty()) {
                throw IllegalArgumentException("players count should be bigger then zero")
            }
            return MeetingDecreaseFactory.getNewSeatingWithMinRating(generateSomeLegalSeating(players, roundCount))
        }

        private fun generateSomeLegalSeating(players: List<Player>,roundCount: Int): TourSeating {
            return TourSeating(Array(roundCount) { generateSomeLegalRound(players, roundCount) }.toMutableList())
        }

        private fun generateSomeLegalRound(players: List<Player>,roundCount: Int): RoundSeating {
            val someRound = generateSomeIllegalRound(players, roundCount)
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
            if (!someRound.isLegalRound) {
                throw Exception("Error while generating seating. Round is not legal")
            }
            return someRound
        }

        private fun generateSomeIllegalRound(players: List<Player>,roundCount: Int): RoundSeating {
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
}