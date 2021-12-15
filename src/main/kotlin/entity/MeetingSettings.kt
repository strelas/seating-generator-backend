package entity

import kotlin.math.max
import kotlin.math.min

class MeetingSettings constructor(
    private val list: List<MutableList<Int>>,
    private val players: List<Player>
) {

    val rating: Pair<Int,Int>
        get() {
            var rating = 0
            var countOfSameRating = 0
            for (playersList in list.withIndex()) {
                var min = 10000
                var max = 0
                for (count in playersList.value.withIndex()) {
                    if (count.index == playersList.index) {
                        continue
                    }
                    if (players[playersList.index].cannotMeet.contains(players[count.index].nickname)) {
                        continue
                    }
                    min = min(count.value, min)
                    max = max(count.value, max)
                }
                if (min == 0) {
                    min-=4
                }
                if (rating == max-min) {
                    countOfSameRating++
                } else if (rating < max-min) {
                    rating = max-min
                    countOfSameRating = 1
                }
            }
            return Pair(rating, countOfSameRating)
        }

    fun decreaseMeeting(first: Player, second: Player) {
        val indexOfFirst = players.indexOf(first)
        val indexOfSecond = players.indexOf(second)
        list[indexOfFirst][indexOfSecond]--
        list[indexOfSecond][indexOfFirst]--
    }

    fun increaseMeeting(first: Player, second: Player) {
        val indexOfFirst = players.indexOf(first)
        val indexOfSecond = players.indexOf(second)
        list[indexOfFirst][indexOfSecond]++
        list[indexOfSecond][indexOfFirst]++
    }

    override fun toString(): String {
        var result = "Статистика пересечений\n"
        for (player in players.indices) {
            result += "${players[player].nickname}:\n"
            for (anotherPlayer in players.indices) {
                if (player == anotherPlayer) {
                    continue
                }
                result+="${players[anotherPlayer].nickname}=${max(list[player][anotherPlayer], 0)};"
            }
            result += "\n"
        }
        return result
    }

    companion object {
        fun createFromSeating(seating: TourSeating): MeetingSettings {
            val players = ArrayList<Player>()
            for (table in seating.rounds.first().tables) {
                players.addAll(table.players)
            }
            val list = List(players.size) {
                MutableList(players.size) { 0 }
            }
            for (round in seating.rounds) {
                for (table in round.tables) {
                    for (first in table.players) {
                        for (second in table.players) {
                            if (first == second) {
                                continue
                            }
                            list[players.indexOf(first)][players.indexOf(second)]++
                        }
                    }
                }
            }
            return MeetingSettings(list, players)
        }
    }
}