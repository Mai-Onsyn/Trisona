package mai_onsyn.trisona.core.sql

import com.alibaba.fastjson2.JSONArray
import mai_onsyn.trisona.core.data.Date
import mai_onsyn.trisona.core.message.PlayListInfo
import mai_onsyn.trisona.core.message.UniversalPath
import mai_onsyn.trisona.core.play.PlayList
import mai_onsyn.trisona.core.utils.HashUtil

class PlayListSQL(instance: SQLInstance): SQLOperator(instance) {
    companion object {
        const val TABLE_NAME = "PlayList"
    }

    init {
        instance.statement.execute("""
            create table if not exists $TABLE_NAME (
                id integer primary key autoincrement,
                name text,
                creator text,
                create_date integer,
                cover_path_net text,
                cover_path_loc text,
                contents text
            )
        """.trimIndent())
    }

    fun storage(playList: PlayList) {
        with(playList) {
            val id = HashUtil.stringToNegativeLong(info.name)
            insert(TABLE_NAME, id, "name", info.name)
            insert(TABLE_NAME, id, "creator", info.creator)
            insert(TABLE_NAME, id, "create_date", info.createDate.time)
            if (info.coverPath.netWorkUrl != null)
                insert(TABLE_NAME, id, "cover_path_net", info.coverPath.netWorkUrl)
            if (info.coverPath.nativePath != null)
                insert(TABLE_NAME, id, "cover_path_loc", info.coverPath.nativePath)
            insert(TABLE_NAME, id, "contents", playList.mapNotNull { it.id })
        }
    }

    fun query(name: String, sqls: SQLPackage): PlayList? {
        return query(HashUtil.stringToNegativeLong(name), sqls)
    }

    fun query(id: Long, sqls: SQLPackage): PlayList? {
        if (!contains(id)) return null
        return PlayList(
            PlayListInfo().apply {
                name = queryString(TABLE_NAME, id, "name")
                creator = queryString(TABLE_NAME, id, "creator")
                createDate = Date(queryLong(TABLE_NAME, id, "create_date") ?: 0)
                coverPath = UniversalPath(
                    queryString(TABLE_NAME, id, "cover_path_net"),
                    queryString(TABLE_NAME, id, "cover_path_loc")
                )
            }
        ).apply {
            queryString(TABLE_NAME, id, "contents")?.let {
                JSONArray.parseArray(it).forEach { idStr ->
                    sqls.musicSQL.query(idStr as Long, sqls.audioSQL, sqls.artistSQL)?.let { music -> add(music) }
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