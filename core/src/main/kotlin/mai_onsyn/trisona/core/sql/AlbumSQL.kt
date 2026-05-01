package mai_onsyn.trisona.core.sql

import com.alibaba.fastjson2.JSONArray
import mai_onsyn.trisona.core.data.Album
import mai_onsyn.trisona.core.log

class AlbumSQL(instance: SQLInstance): SQLOperator(instance) {
    companion object {
        const val TABLE_NAME = "Album"
    }
    
    init {
        instance.statement.execute("""
            create table if not exists Album (
                id integer primary key autoincrement,
                name text,
                pic_net text,
                pic_loc text,
                content_id text
            )
        """.trimIndent())
    }

    fun storage(album: Album) = with(album) {
        if (id == -1L) {
            log.error("Album id has not been initialized, title {}", name)
            return@with
        }
        insert(TABLE_NAME, id, "name", name)
        if (picUrlNet != null)
            insert(TABLE_NAME, id, "pic_net", picUrlNet)
        if (picUrlNative != null)
            insert(TABLE_NAME, id, "pic_loc", picUrlNative)
        if (album.isNotEmpty()) {
            val list = mutableListOf<Long>()
            queryString(id, "content_id")?.let {
                JSONArray.parseArray(it).map {
                        any -> list.add(any.toString().toLong()) //先查出所有的内容合并之后再写入
                }
            }
            list.addAll(album)
            insert(TABLE_NAME, id, "content_id", list.distinct())
        }
    }

    fun query(id: Long): Album? {
        if (!exists(TABLE_NAME, id)) return null
        return Album().apply {
            this.id = id
            name = queryString(id, "name")
            picUrlNet = queryString(id, "pic_net")
            picUrlNative = queryString(id, "pic_loc")

            queryString(id, "content_id")?.let {
                JSONArray.parseArray(it).map {
                    any -> add(any.toString().toLong())
                }
            }
        }
    }

    fun contains(id: Long): Boolean = super.exists(TABLE_NAME, id)
    fun queryLong(id: Long, column: String): Long? = super.queryLong(TABLE_NAME, id, column)
    fun queryInt(id: Long, column: String): Int? = super.queryInt(TABLE_NAME, id, column)
    fun queryDouble(id: Long, column: String): Double? = super.queryDouble(TABLE_NAME, id, column)
    fun queryBoolean(id: Long, column: String): Boolean? = super.queryBoolean(TABLE_NAME, id, column)
    fun queryString(id: Long, column: String): String? = super.queryString(TABLE_NAME, id, column)
}