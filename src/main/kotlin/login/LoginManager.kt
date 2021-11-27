package login

class LoginManager {
    private val repository = LoginRepository()

    fun login(login: String, password: String): Boolean {
        return repository.getDataFor(login).password == password
    }
}