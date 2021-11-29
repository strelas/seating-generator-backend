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

    fun changePassword(login: String, newPassword: String) {
        try {
            database.execute("UPDATE default_schema.users t SET t.password = '$newPassword' " +
                    "WHERE t.login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {}
    }

    fun insertUser(login: String, password: String) {
        try {
            database.execute("insert into default_schema.users (login, password) values ('$login', '$password')")
        } catch (e: Exception) {}
    }

    fun deleteUser(login: String) {
        try {
            database.execute("DELETE FROM default_schema.users WHERE login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {}
    }

    fun getTournamentIds(login: String): List<Int> {
        return try {
            val set = database.executeQuery("select tournaments from default_schema.users where login = '$login';")
            set.next()
            set.getString(1).split(",").filter { it.isNotEmpty() }.map { it.toInt() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun addTournament(login: String, id: Int): Boolean {
        return try {
            database.execute("UPDATE default_schema.users t SET t.tournaments = '${getTournamentIds(login).run { 
                "," + this.toMutableList().apply { add(id) }.joinToString(",,") + ","
            }}' " +
                    "WHERE t.login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {
            false
        }
    }

    fun removeTournament(login: String, id: Int): Boolean {
        return try {
            database.execute("UPDATE default_schema.users t SET t.tournaments = '${getTournamentIds(login).run {
                "," + this.toMutableList().apply { remove(id) }.joinToString(",,") + ","
            }}' " +
                    "WHERE t.login LIKE '$login' ESCAPE '#';")
        } catch (e: Exception) {
            false
        }
    }

    fun removeTournamentFromAllUsers(id: Int) {
        try {
            val set = database.executeQuery("select login from default_schema.users where tournaments like '%,$id,%';")
            while (set.next()) {
                val login = set.getString(1)
                removeTournament(login, id)
            }
        } catch (e: Exception) {}
    }
}