package entity

enum class Skill {
    A,
    B,
    C
}

data class Player(
    val nickname: String,
    val fiimNickname: String,
    val skill: Skill,
    val cannotMeet: List<String>,
    val place: Int = 0
) {
    companion object {
        fun withNicknameOnly(nickname: String): Player = Player(nickname, nickname, Skill.A, arrayListOf())
    }
}