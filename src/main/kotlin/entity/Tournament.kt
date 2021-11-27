package entity

data class Tournament constructor(
    val players: List<Player>,
    val seating: TourSeating?,
    val settings: TournamentSettings = TournamentSettings.default()
)