package entity

data class TourSeating(val rounds: MutableList<RoundSeating>) {

    fun parallelSum(other: TourSeating): TourSeating {
        if (other.rounds.size != rounds.size) {
            throw IllegalArgumentException("for parallel sum rounds size should be equal")
        }
        val newRounds = arrayListOf<RoundSeating>()
        for (index in rounds.indices) {
            newRounds.add(RoundSeating(arrayListOf<TableSeating>().apply {
                addAll(rounds[index].tables.map { it.copy() })
                addAll(other.rounds[index].tables.map { it.copy() })
            }))
        }

        return TourSeating(newRounds)
    }

    fun syncSum(other: TourSeating): TourSeating {
        if (other.rounds.first().tables != rounds.first().tables) {
            throw IllegalArgumentException("for sync sum tables size should be equal")
        }

        return TourSeating(arrayListOf<RoundSeating>().apply {
            addAll(rounds.map { it.copy() })
            addAll(other.rounds.map { it.copy() })
        })
    }

    override fun toString(): String {
        return rounds.mapIndexed { index, roundSeating -> "Раунд ${index + 1}\n$roundSeating" }.joinToString("\n")
    }
}

