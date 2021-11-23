package entity

enum class Skill {
    A,
    B,
    C
}

data class Player(val nickname: String, val fiimNickname: String, val skill: Skill, val cannotMeet: List<String>)