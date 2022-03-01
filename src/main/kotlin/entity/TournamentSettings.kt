package entity

data class TournamentSettings(val isSwiss: Boolean, val swissSplit: List<Int>) {
    companion object {
        fun default() = TournamentSettings(false, arrayListOf())
    }
}