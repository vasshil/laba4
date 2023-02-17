package com.example.laba4

import sun.security.util.Password
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class DataBaseController {

    private var connection: Connection? = null

    init {

        try {
            connection = DriverManager.getConnection(urlConnect, dataBaseLogin, dataBasePassword)
            println(getUsersFromTable())
            println(getUserRole("admin", "admin"))
        } catch (ignored: SQLException) {
            println("failed---------------")
        }
    }


    @Throws(SQLException::class)
    fun getUsersFromTable(): MutableList<UserData> {

        val task = "select * from $dataBaseName;"
        val resultSet = sendStatement(task, true) as ResultSet


        val userData = mutableListOf<UserData>()


        while (resultSet.next()) {
            userData += UserData(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
//                Roles.values()[resultSet.getInt(3)],
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6),
                resultSet.getString(7)
            )
        }

        return userData
    }

    fun getUserRole(username: String, password: String): Roles {
        val task = "select job from \"user\" where username = '$username' and password = '$password';"
        val result = sendStatement(task, true) as ResultSet
        while (result.next()) {
            return Roles.values()[result.getInt("job")]
        }

        return Roles.NONE

    }

    fun deleteUserById(id: Int) {
        val task = "delete from \"user\" where id = $id;"
        sendStatement(task, false)
    }

    fun updateUserByIs(id: Int, user: UserData) {
        val task = "update \"user\" set fullName = '${user.fullName}', job = '${user.job}', address = '${user.address}', phoneNumber = '${user.phoneNumber}', username = '${user.username}', password = '${user.password}' where id = $id;"
        sendStatement(task, false)
    }

    fun addNewUser(user: UserData) {
        val task = "insert into \"user\"(fullName, job, address, phoneNumber, username, password)" +
                "values ('${user.fullName}', ${user.job}, '${user.address}', '${user.phoneNumber}', '${user.username}', '${user.password}');"
        sendStatement(task, false)
    }

    @Throws(SQLException::class)
    fun sendStatement(task: String, isSelecting: Boolean): Any {
        val statement = connection!!.createStatement()
        return if (isSelecting) statement.executeQuery(task) else statement.executeUpdate(task)
    }

}