import login.LoginManager
import sql.MySQLDataBase
import sql.UsersRepository

class AppAssembly {
    private val sqlDataBase by lazy {
        return@lazy MySQLDataBase()
    }

    private val usersTableWrapper by lazy {
        return@lazy UsersRepository(sqlDataBase)
    }

    val loginManager by lazy {
        return@lazy LoginManager(usersTableWrapper)
    }
}