package sql

import entity.Player
import entity.Skill

class TournamentRepository constructor(private val database: MySQLDataBase, private val id: Int) {
    private val table = "default_schema.tournament_$id"

    init {
        val set = database.executeQuery("show tables from default_schema like 'tournament_$id';")
        if (!set.next()) {
            throw Exception("Table is not exist")
            database.execute(
                "create table $table\n" +
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
            val set = database.executeQuery("select * from $table where nickname = '${player.nickname}';")
            set.next()
        } catch (e: Exception) {
            false
        }
    }

    fun appendPlayer(player: Player) {
        try {
            if (containsPlayer(player)) {
                database.execute(
                    "UPDATE $table t\n" +
                            "SET t.fiimnick = '${player.fiimNickname}',\n" +
                            "    t.skill    = ${player.skill.ordinal},\n" +
                            "    t.cannot_meet = '${player.cannotMeet.joinToString(",")}',\n" +
                            "    t.place = ${player.place}\n" +
                            "WHERE t.nickname LIKE '${player.nickname}' ESCAPE '#';"
                )
            } else {
                database.execute(
                    "INSERT INTO $table (nickname, fiimnick, skill, cannot_meet, place) " +
                            "VALUES ('${player.nickname}', '${player.nickname}', ${player.skill.ordinal}, '${
                                player.cannotMeet.joinToString(",")
                            }', ${player.place});"
                )
            }
        } catch (e: Exception) {
        }
    }

    fun removePlayer(player: Player) {
        try {
            database.execute("delete from $table where nickname like '${player.nickname}' escape '#'")
        } catch (e: Exception) {
        }
    }

    fun getPlayers(): List<Player> {
        try {
            val result = ArrayList<Player>()
            val set = database.executeQuery("select * from $table;")
            while (set.next()) {
                result.add(
                    Player(
                        set.getString("nickname"),
                        set.getString("fiimnick"),
                        Skill.values()[set.getInt("skill")],
                        set.getString("cannot_meet").split(",")
                    )
                )
            }
            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }
}