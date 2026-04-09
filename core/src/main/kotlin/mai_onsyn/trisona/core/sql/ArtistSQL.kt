package mai_onsyn.trisona.core.sql

import com.alibaba.fastjson2.JSONArray
import mai_onsyn.trisona.core.message.Artist

class ArtistSQL(instance: SQLInstance): SQLOperator(instance) {
    companion object {
        const val TABLE_NAME = "Artist"
    }
    
    init {
        instance.statement.execute("""
            create table if not exists Artist (
                id integer primary key autoincrement,
                name text,
                alias text
            )
        """.trimIndent())
    }

    fun storage(artist: Artist) = with(artist) {
        insert(TABLE_NAME, id, "name", name)
        insert(TABLE_NAME, id, "alias", alias)
    }

    fun query(id: Long): Artist? {
        if (!contains(id)) return null

        return Artist(
            id,
            queryString(TABLE_NAME, id, "name"),
            queryString(TABLE_NAME, id, "alias")?.let {
                JSONArray.parseArray(it).map { any -> any.toString() }
            }
        )
    }

    fun contains(id: Long): Boolean = super.exists(TABLE_NAME, id)
    fun queryLong(id: Long, column: String): Long? = super.queryLong(TABLE_NAME, id, column)
    fun queryInt(id: Long, column: String): Int? = super.queryInt(TABLE_NAME, id, column)
    fun queryDouble(id: Long, column: String): Double? = super.queryDouble(TABLE_NAME, id, column)
    fun queryBoolean(id: Long, column: String): Boolean? = super.queryBoolean(TABLE_NAME, id, column)
    fun queryString(id: Long, column: String): String? = super.queryString(TABLE_NAME, id, column)
}