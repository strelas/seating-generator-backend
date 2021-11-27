import login.LoginManager
import sql.MySQLDataBase
import sql.TournamentRepository
import sql.UsersRepository

class AppAssembly {
    private val sqlDataBase by lazy {
        return@lazy MySQLDataBase()
    }

    private val usersRepository by lazy {
        return@lazy UsersRepository(sqlDataBase)
    }

    fun getTournamentRepository(id: Int) = TournamentRepository(sqlDataBase, id)

    val loginManager by lazy {
        return@lazy LoginManager(usersRepository)
    }
}