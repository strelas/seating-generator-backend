package entity

data class TournamentSettings(val isSwiss: Boolean, val swissSplit: List<Int>) {

    fun copyWith(isSwiss: Boolean) = TournamentSettings(isSwiss, swissSplit)

    fun copyWith(swissSplit: List<Int>) = TournamentSettings(isSwiss, swissSplit)

    companion object {
        fun default() = TournamentSettings(false, arrayListOf())
    }
}