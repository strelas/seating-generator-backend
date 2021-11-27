package sql

import java.sql.*
import java.util.*

class MySQLDataBase {
    private val connection: Connection
    init {
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?characterEncoding=utf8", "root", "lkjhlkjh")
    }

    fun executeQuery(command: String): ResultSet {
        return connection.createStatement().executeQuery(command)
    }

    fun execute(command: String): Boolean {
        return connection.createStatement().execute(command)
    }
}