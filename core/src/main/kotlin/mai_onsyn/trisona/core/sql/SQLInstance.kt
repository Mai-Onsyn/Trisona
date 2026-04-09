package mai_onsyn.trisona.core.sql

import mai_onsyn.trisona.core.MultiplatformInterface.USER_DIRECTORY
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import kotlin.use

class SQLInstance {
    val connection: Connection
    val statement: Statement

    constructor(dataBaseName: String) {
        connection = DriverManager.getConnection("jdbc:sqlite:$USER_DIRECTORY/$dataBaseName.db")
        statement = connection.createStatement()
    }

    fun printAllTables() {
        val tableNames = mutableListOf<String>()
        connection.createStatement().use { stmt ->
            stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'").use { rs ->
                while (rs.next()) {
                    tableNames.add(rs.getString("name"))
                }
            }
        }

        tableNames.forEach { tableName ->
            println("\n--- $tableName ---")
            connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM $tableName").use { rs ->
                    val metaData = rs.metaData
                    val columnCount = metaData.columnCount

                    val header = (1..columnCount).joinToString(" | ") { metaData.getColumnName(it) }
                    println(header)
                    println("-".repeat(header.length))

                    while (rs.next()) {
                        val row = (1..columnCount).joinToString(" | ") { rs.getString(it) ?: "NULL" }
                        println(row)
                    }
                }
            }
        }
    }
}