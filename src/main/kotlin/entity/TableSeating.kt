package entity

data class TableSeating(val players: MutableList<Player>, val referee: String) {

    val isLegalTable: Boolean
        get() {
            return players.all { playerCanSitHere(it, it) }
        }

    fun playerCanSitHere(newPlayer: Player, insteadOf: Player): Boolean {
        for (player in players) {
            if (insteadOf == player) {
                continue
            }

            if (player.cannotMeet.contains(newPlayer.nickname)
                || newPlayer.cannotMeet.contains(player.nickname)
                || newPlayer.cannotMeet.contains(referee)
            ) {
                return false
            }

        }
        return true
    }

    override fun toString(): String {
        return "Судья: $referee\n${players.mapIndexed { index, player ->  "${index+1}. $player"}.joinToString("\n")}"
    }
}