import mai_onsyn.trisona.core.MultiplatformInterface.USER_DIRECTORY
import java.sql.DriverManager

fun main() {
    val connection = DriverManager.getConnection("jdbc:sqlite:$USER_DIRECTORY/test.db")
    val statement = connection.createStatement()

    statement.execute(
        """
            create table if not exists test (
                id integer primary key autoincrement, 
                name text not null,
                var1 text,
                var2 text
            )
            """.trimIndent()
    )

    statement.execute(
        """
            insert into test (id, name, var1, var2) values (100, 'row1', 'value1', 'value2')
            on conflict(id) do update set
                name = excluded.name,
                var1 = excluded.var1,
                var2 = excluded.var2
        """.trimIndent()
    )

    statement.execute(
        """
            insert into test (id, name, var2) values (100, 'changed row', 'value2 changed')
            on conflict(id) do update set
                name = excluded.name,
                var2 = excluded.var2
        """.trimIndent()
    )

    val rs = statement.executeQuery(
        """
        select * from test
        """.trimIndent()
    )
    println("\n--- Test ---")
    while (rs.next()) {
        println("ID: ${rs.getInt("id")}, name: ${rs.getString("name")}, var1: ${rs.getString("var1")}, var2: ${rs.getString("var2")}")
    }
}