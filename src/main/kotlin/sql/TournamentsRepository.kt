package sql

import entity.Tournament
import kotlin.random.Random

class TournamentsRepository constructor(
    private val database: MySQLDataBase,
    private val usersRepository: UsersRepository
) {
    fun getTournamentById(id: Int): Tournament? {
        val set = database.executeQuery("select name from default_schema.tournaments where id = $id;")
        return if (set.next()) {
            val name = set.getString(1)
            Tournament(id, name)
        } else {
            null
        }
    }

    fun changeGamesCount(id: Int, finalGames: Int, beforeSwiss: Int, afterSwiss: Int) {
        database.execute("UPDATE default_schema.tournaments t\n" +
                "SET t.games_before_swiss = $beforeSwiss,\n" +
                "    t.games_after_swiss  = $afterSwiss,\n" +
                "    t.final_games        = $finalGames\n" +
                "WHERE t.id = $id;")
    }

    fun createTournament(name: String, owner: String, finalGames: Int, beforeSwiss: Int, afterSwiss: Int) {
        var id: Int
        do {
            id = Random.nextInt(1, 10000)
        } while (getTournamentById(id) != null)
        database.execute(
            "create table default_schema.tournament_$id\n" +
                    "(\n" +
                    "    nickname    varchar(50)             not null,\n" +
                    "    fiimnick    varchar(50)             not null,\n" +
                    "    skill       int                      not null,\n" +
                    "    cannot_meet varchar(500) default '' not null,\n" +
                    "    place int default 0 not null,\n" +
                    "    constraint players_pk\n" +
                    "        primary key (nickname)\n" +
                    ");\n" +
                    "\n"
        )
        database.execute(
            "INSERT INTO default_schema.tournaments (id, name, games_before_swiss, games_after_swiss, final_games)\n" +
                    "VALUES ($id, '$name', $beforeSwiss, $afterSwiss, $finalGames);"
        )
        usersRepository.addTournament(owner, id)
    }
}