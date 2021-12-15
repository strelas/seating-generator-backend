package sql

import entity.Tournament
import kotlin.random.Random

class TournamentsRepository constructor(private val database: MySQLDataBase, private val usersRepository: UsersRepository) {
    fun getTournamentById(id: Int): Tournament? {
        val set = database.executeQuery("select name from default_schema.tournaments where id = $id;")
        return if (set.next()) {
            val name = set.getString(1)
            Tournament(id, name)
        } else {
            null
        }
    }

    fun createTournament(name: String, owner: String) {
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
        database.execute("INSERT INTO default_schema.tournaments (id, name) VALUES ($id, '$name');")
        usersRepository.addTournament(owner, id)
    }
}