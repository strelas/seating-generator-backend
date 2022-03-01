package factory

import entity.*

class MeetingDecreaseFactory {
    companion object {
        fun getNewSeatingWithMinRating(seating: TourSeating): TourSeating {
            val meetingSettings = MeetingSettings.createFromSeating(seating)
            val rating = meetingSettings.rating
            println("${rating.first}:${rating.second}")
            var value = decreaseIteration(seating, meetingSettings)
            while (value.second) {
                value = decreaseIteration(value.first, meetingSettings)
            }
            println(meetingSettings)
            return value.first
        }

        private fun decreaseIteration(
            seating: TourSeating,
            meetingSettings: MeetingSettings
        ): Pair<TourSeating, Boolean> {
            for (roundIndex in seating.rounds.indices) {
                for (firstTableIndex in seating.rounds[roundIndex].tables.indices) {
                    for (firstPlayer in seating.rounds[roundIndex].tables[firstTableIndex].players) {
                        for (secondTableIndex in firstTableIndex until seating.rounds[roundIndex].tables.size) {
                            for (secondPlayer in seating.rounds[roundIndex].tables[secondTableIndex].players) {
                                if (!seating.rounds[roundIndex].tables[firstTableIndex].playerCanSitHere(
                                        secondPlayer,
                                        firstPlayer
                                    )
                                    || !seating.rounds[roundIndex].tables[secondTableIndex].playerCanSitHere(
                                        firstPlayer,
                                        secondPlayer
                                    )
                                    || firstPlayer.skill != secondPlayer.skill
                                ) {
                                    continue
                                }
                                val result = iterate(
                                    roundIndex,
                                    firstTableIndex,
                                    firstPlayer,
                                    secondTableIndex,
                                    secondPlayer,
                                    seating,
                                    meetingSettings
                                )
                                result ?: continue
                                val newRating = meetingSettings.rating
                                println("${newRating.first}.${newRating.second}")
                                return Pair(result, true)
                            }
                        }
                    }
                }
            }
            return Pair(seating, false)
        }

        private fun iterate(
            roundIndex: Int,
            firstTableIndex: Int,
            firstPlayer: Player,
            secondTableIndex: Int,
            secondPlayer: Player,
            tourSeating: TourSeating,
            settings: MeetingSettings
        ): TourSeating? {
            val ratingBefore = settings.rating
            val resetMeeting = {
                for (anotherPlayer in tourSeating.rounds[roundIndex].tables[firstTableIndex].players) {
                    if (anotherPlayer == firstPlayer || anotherPlayer == secondPlayer) {
                        continue
                    }
                    settings.increaseMeeting(firstPlayer, anotherPlayer)
                    settings.decreaseMeeting(secondPlayer, anotherPlayer)
                }
                for (anotherPlayer in tourSeating.rounds[roundIndex].tables[secondTableIndex].players) {
                    if (anotherPlayer == firstPlayer || anotherPlayer == secondPlayer) {
                        continue
                    }
                    settings.decreaseMeeting(firstPlayer, anotherPlayer)
                    settings.increaseMeeting(secondPlayer, anotherPlayer)
                }
            }

            for (anotherPlayer in tourSeating.rounds[roundIndex].tables[firstTableIndex].players) {
                if (anotherPlayer == firstPlayer || anotherPlayer == secondPlayer) {
                    continue
                }
                settings.decreaseMeeting(firstPlayer, anotherPlayer)
                settings.increaseMeeting(secondPlayer, anotherPlayer)
            }
            for (anotherPlayer in tourSeating.rounds[roundIndex].tables[secondTableIndex].players) {
                if (anotherPlayer == firstPlayer || anotherPlayer == secondPlayer) {
                    continue
                }
                settings.increaseMeeting(firstPlayer, anotherPlayer)
                settings.decreaseMeeting(secondPlayer, anotherPlayer)
            }
            val newRating = settings.rating
            if (newRating.first > ratingBefore.first) {
                resetMeeting()
                return null
            }
            if (newRating.second > ratingBefore.second && newRating.first == ratingBefore.first) {
                resetMeeting()
                return null
            }
            if (newRating.first == ratingBefore.first && newRating.second == ratingBefore.second) {
                resetMeeting()
                return null
            }
            val seating = TourSeating(tourSeating.rounds.map { round ->
                RoundSeating(round.tables.map { table ->
                    TableSeating(table.players.map { player ->
                        when (player) {
                            firstPlayer -> {
                                secondPlayer
                            }
                            secondPlayer -> {
                                firstPlayer
                            }
                            else -> {
                                player
                            }
                        }
                    }.toMutableList(), table.referee)
                }.toMutableList())
            }.toMutableList())
            if (!seating.rounds.all { it.isLegalRound }) {
                resetMeeting()
                return null
            }
            return seating
        }
    }
}