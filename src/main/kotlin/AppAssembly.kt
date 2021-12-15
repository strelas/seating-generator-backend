import login.LoginManager
import sql.MySQLDataBase
import sql.TournamentRepository
import sql.TournamentsRepository
import sql.UsersRepository

class AppAssembly {
    private val sqlDataBase by lazy {
        return@lazy MySQLDataBase()
    }

    private val usersRepository by lazy {
        return@lazy UsersRepository(sqlDataBase)
    }

    val tournamentsRepository by lazy {
        return@lazy TournamentsRepository(sqlDataBase, usersRepository)
    }

    fun getTournamentRepository(id: Int) = TournamentRepository(sqlDataBase, id)

    val loginManager by lazy {
        return@lazy LoginManager(usersRepository)
    }
}