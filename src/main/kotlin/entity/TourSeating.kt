package entity

data class TourSeating(val rounds: MutableList<RoundSeating>) {
    override fun toString(): String {
        return rounds.mapIndexed { index, roundSeating -> "Раунд ${index + 1}\n$roundSeating" }.joinToString("\n")
    }
}

