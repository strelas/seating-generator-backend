package sql

import entity.Tournament

class TournamentsRepository constructor(private val database: MySQLDataBase) {
    fun getTournamentById(id: Int): Tournament {
        val set = database.executeQuery("select name from default_schema.tournaments where id = $id;")
        set.next()
        val name = set.getString(1)
        return Tournament(id, name)
    }
}