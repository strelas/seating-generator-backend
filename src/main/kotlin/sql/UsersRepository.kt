package sql

class UsersRepository constructor(private val database: MySQLDataBase) {
    fun checkPassword(login: String, password: String): Boolean {
        try {
            val set = database.executeQuery("select password from default_schema.users where login = '$login';")
            if(set.next()) {
                return set.getString(1) == password
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    fun containsUser(login: String): Boolean {
        return try {
            val set = database.executeQuery("select * from default_schema.users where login = '$login';")
            set.next()
        } catch (e: Exception) {
            false
        }
    }

    fun changePassword(login: String, newPassword: String): Boolean {
        return try {
            database.execute("UPDATE default_schema.users t SET t.password = '$newPassword' " +
                    "WHERE t.login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {
            false
        }
    }

    fun insertUser(login: String, password: String): Boolean {
        return try {
            database.execute("insert into default_schema.users (login, password) values ('$login', '$password')")
        } catch (e: Exception) {
            false
        }
    }

    fun deleteUser(login: String): Boolean {
        return try {
            database.execute("DELETE FROM default_schema.users WHERE login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {
            false
        }
    }
}