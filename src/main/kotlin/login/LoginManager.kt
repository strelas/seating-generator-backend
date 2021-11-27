package login

import sql.UsersRepository

class LoginManager constructor(private val usersRepository: UsersRepository) {
    fun login(login: String, password: String): Boolean {
        return usersRepository.checkPassword(login, password)
    }

    fun signUp(login: String, password: String): Boolean {
        return if (usersRepository.containsUser(login)) {
            false
        } else {
            usersRepository.insertUser(login, password)
        }
    }

    fun changePassword(login: String, newPassword: String): Boolean {
        return if (usersRepository.containsUser(login)) {
            false
        } else {
            usersRepository.changePassword(login, newPassword)
        }
    }
}