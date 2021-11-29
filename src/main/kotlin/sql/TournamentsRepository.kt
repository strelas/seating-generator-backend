package sql

import entity.Tournament
import kotlin.random.Random

class TournamentsRepository constructor(private val database: MySQLDataBase) {
    fun getTournamentById(id: Int): Tournament? {
        val set = database.executeQuery("select name from default_schema.tournaments where id = $id;")
        return if (set.next()) {
            val name = set.getString(1)
            Tournament(id, name)
        } else {
            null
        }
    }

    fun createTournament(name: String) {
        var id: Int
        do {
            id = Random.nextInt()
        } while (getTournamentById(id) != null)
        database.execute("INSERT INTO default_schema.tournaments (id, name) VALUES ($id, '$name');")
    }
}