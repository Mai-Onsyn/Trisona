package mai_onsyn.trisona.core.sql

import com.alibaba.fastjson2.JSONArray
import mai_onsyn.trisona.Global
import mai_onsyn.trisona.core.data.MusicQuality
import mai_onsyn.trisona.core.log
import mai_onsyn.trisona.core.message.Artist
import mai_onsyn.trisona.core.message.Music
import mai_onsyn.trisona.core.message.UniversalPath
import java.util.ArrayList

class MusicSQL(instance: SQLInstance): SQLOperator(instance) {
    companion object {
        const val TABLE_NAME = "Music"
    }
    init {
        instance.statement.execute("""
            create table if not exists Music (
                id integer primary key autoincrement,
                net_path text,
                loc_path text,
                title text,
                alias text,
                artists text,
                album_id integer,
                popularity real,
                duration integer,
                fee integer,
                current_quality text,
                supports_quality text,
                q_standard integer,
                q_higher integer,
                q_exhigh integer,
                q_lossless integer,
                q_hires integer,
                q_jyeffect integer,
                q_sky integer,
                q_dolby integer,
                q_jymaster integer,
                q_native integer
            )
        """.trimIndent())
    }

    fun storage(mMsg: Music, sqls: SQLPackage) {
        val audioSQL = sqls.audioSQL
        val artistSQL = sqls.artistSQL
        if (mMsg.id == -1L) {
            log.error("MusicMessage id has not been initialized, title ${mMsg.title}")
            return
        }
        with(mMsg) {
            if (audioPath.netWorkUrl?.isNotEmpty()?: false)
                insert(TABLE_NAME, id, "net_path", audioPath.netWorkUrl)
            if (audioPath.nativePath?.isNotEmpty()?: false)
                insert(TABLE_NAME, id, "loc_path", audioPath.nativePath)
            if (title?.isNotEmpty()?: false)
                insert(TABLE_NAME, id, "title", title)
            if (alias.isNotEmpty()) //List<String>自动处理序列号
                insert(TABLE_NAME, id, "alias", alias)
            if (artists.isNotEmpty()) //存id列表
                insert(TABLE_NAME, id, "artists", artists.map { it.id })
            if (albumID != -1L) //指向Album表的唯一id
                insert(TABLE_NAME, id, "album_id", albumID)
            if (popularity != -1.0)
                insert(TABLE_NAME, id, "popularity", popularity)
            if (duration != -1)
                insert(TABLE_NAME, id, "duration", duration)
            insert(TABLE_NAME, id, "fee", fee)
            if (currentQuality != null)
                insert(TABLE_NAME, id, "current_quality", currentQuality.name)
            if (supportsQualities.isNotEmpty()) //存支持的质量名字列表
                insert(TABLE_NAME, id, "supports_quality", supportsQualities.map { it.name })

            allAudioMessages.forEach { (quality, pack) ->
                val qid = queryLong(id, "q_${quality.id}")?: Global.RANDOM.nextLong(0, Long.MAX_VALUE)
                insert(TABLE_NAME, id, "q_${quality.id}", qid)
//                println(quality)
                audioSQL.storage(qid, id, pack.completeAudioInfo)
            }

            artists.forEach { artistSQL.storage(it) }
        }
    }

    fun query(id: Long, audioSQL: AudioSQL, artistSQL: ArtistSQL): Music? {
        if (!contains(id)) return null

        return Music().apply {
            this.id = id
            audioPath = UniversalPath().apply {
                netWorkUrl = queryString(id, "net_path")?: ""
                nativePath = queryString(id, "loc_path")?: ""
            }
            title = queryString(id, "title")
            alias = queryString(id, "alias")?.let {
                JSONArray.parseArray(it).map { any -> any.toString() }
            }?: ArrayList<String>()
            artists = queryString(id, "artists")?.let {
                JSONArray.parseArray(it).map {
                    aID -> artistSQL.query(aID.toString().toLong())
                }
            }?: ArrayList<Artist>()
            albumID = queryLong(id, "album_id")?: -1
            popularity = queryDouble(id, "popularity")?: -1.0
            duration = queryInt(id, "duration")?: -1
            fee = queryBoolean(id, "fee")?: false
            currentQuality = MusicQuality.valueOf(queryString(id, "current_quality")?: "STANDARD")
            queryString(id, "supports_quality")?.let {
                JSONArray.parseArray(it).map {
                    any -> MusicQuality.valueOf(any.toString())
                }
            }?.forEach { supportsQualities.add(it) }
            mapOf(
                "q_standard" to MusicQuality.STANDARD,
                "q_higher" to MusicQuality.HIGHER,
                "q_exhigh" to MusicQuality.EX_HIGH,
                "q_lossless" to MusicQuality.LOSSLESS,
                "q_hires" to MusicQuality.HI_RES,
                "q_jyeffect" to MusicQuality.JY_EFFECT,
                "q_sky" to MusicQuality.SKY,
                "q_dolby" to MusicQuality.DOLBY,
                "q_jymaster" to MusicQuality.JY_MASTER,
                "q_native" to MusicQuality.NATIVE
            ).forEach { (t, u) ->
//                println(queryLong(id, t))
                if (audioSQL.contains(queryLong(id, t)?: -1)) {
                    allAudioMessages[u] = Music.AudioMessagePackage(audioSQL.query(queryLong(id, t) ?: -1))
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