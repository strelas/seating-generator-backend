package login

import sql.UsersRepository

class LoginManager constructor(private val usersRepository: UsersRepository) {
    fun login(login: String, password: String): Boolean {
        return usersRepository.checkPassword(login, password)
    }

    fun signUp(login: String, password: String) {
        if (!usersRepository.containsUser(login)) {
            usersRepository.insertUser(login, password)
        }
    }

    fun changePassword(login: String, newPassword: String) {
        if (usersRepository.containsUser(login)) {
            usersRepository.changePassword(login, newPassword)
        }
    }
}