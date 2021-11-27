package sql

import entity.Player

class TournamentRepository constructor(private val database: MySQLDataBase, private val id: Int) {
    private val schemaName = "tournament_$id"

    init {
        val set = database.executeQuery("show schemas like '$schemaName';")
        if (!set.next()) {
            database.execute("create schema '$schemaName';")

            database.execute(
                "create table $schemaName.players\n" +
                        "(\n" +
                        "    nickname    varchar(255)             not null,\n" +
                        "    fiimnick    varchar(255)             not null,\n" +
                        "    skill       int                      not null,\n" +
                        "    cannot_meet varchar(1023) default '' not null,\n" +
                        "    place int default 0 not null,\n" +
                        "    constraint players_pk\n" +
                        "        primary key (nickname)\n" +
                        ");\n" +
                        "\n"
            )
        }
    }

    private fun containsPlayer(player: Player): Boolean {
        return try {
            val set = database.executeQuery("select * from $schemaName.players where nickname = '${player.nickname}';")
            set.next()
        } catch (e: Exception) {
            false
        }
    }

    fun appendPlayer(player: Player) {
        try {
            if (containsPlayer(player)) {
                database.execute("UPDATE $schemaName.players t\n" +
                        "SET t.fiimnick = '${player.fiimNickname}',\n" +
                        "    t.skill    = ${player.skill.ordinal},\n" +
                        "    t.cannot_meet = '${player.cannotMeet.joinToString(",")}',\n" +
                        "    t.place = ${player.place}\n" +
                        "WHERE t.nickname LIKE '${player.nickname}' ESCAPE '#';")
            } else {
                database.execute(
                    "INSERT INTO $schemaName.players (nickname, fiimnick, skill, cannot_meet, place) " +
                            "VALUES ('${player.nickname}', '${player.nickname}', ${player.skill.ordinal}, '${
                                player.cannotMeet.joinToString(",")
                            }', ${player.place});"
                )
            }
        } catch (e: Exception) {}
    }

    fun removePlayer(player: Player) {
        try {
            database.execute("delete from $schemaName.players where nickname like '${player.nickname}' escape '#'")
        } catch (e: Exception) {
        }
    }
}