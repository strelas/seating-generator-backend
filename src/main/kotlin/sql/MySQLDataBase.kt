package sql

import java.sql.*
import java.util.*

class MySQLDataBase {
    private val connection: Connection

    init {
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        connection = DriverManager.getConnection("jdbc:mariadb://31.211.71.43:3306?characterEncoding=utf8", "remote", "remote")
    }

    fun executeQuery(command: String): ResultSet {
        return connection.createStatement().executeQuery(command)
    }

    fun execute(command: String): Boolean {
        return connection.createStatement().execute(command)
    }
}