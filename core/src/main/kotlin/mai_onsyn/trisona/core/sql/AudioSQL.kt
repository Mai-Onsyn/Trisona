package mai_onsyn.trisona.core.sql

import mai_onsyn.trisona.core.message.Audio

class AudioSQL(instance: SQLInstance): SQLOperator(instance) {
    companion object {
        const val TABLE_NAME = "Audio"
    }

    init {
        instance.statement.execute("""
            create table if not exists Audio (
                id integer primary key autoincrement,
                music_id integer,
                pcm_byte_length integer,
                file_byte_length integer,
                encoding integer,
                channels integer,
                sample_rate integer,
                bit_depth integer,
                bitrate integer,
                gain integer,
                signed integer,
                foreign key (music_id) references Music(id)
                    on delete cascade
            )
        """.trimIndent())
    }

    fun storage(id: Long, musicID: Long, aMsg: Audio) {
        insert(TABLE_NAME, id, "music_id", musicID)
        with(aMsg) {
            if (pcmByteLength != -1L)
                insert(TABLE_NAME, id, "pcm_byte_length",pcmByteLength)
            if (fileByteLength != -1L)
                insert(TABLE_NAME, id, "file_byte_length", fileByteLength)
            if (encoding != Audio.Encoding.UNKNOWN)
                insert(TABLE_NAME, id, "encoding", encoding.name)
            if (channels != -1)
                insert(TABLE_NAME, id, "channels", channels)
            if (sampleRate != -1)
                insert(TABLE_NAME, id, "sample_rate", sampleRate)
            if (bitDepth != -1)
                insert(TABLE_NAME, id, "bit_depth", bitDepth)
            if (bitRate != -1)
                insert(TABLE_NAME, id, "bitrate", bitRate)
            if (gain != 0.0)
                insert(TABLE_NAME, id, "gain", gain)

            insert(TABLE_NAME, id, "signed", signed)
        }
    }

    fun query(id: Long): Audio? {
        if (!contains(id)) return null
        return Audio().apply {
            pcmByteLength = queryLong(id, "pcm_byte_length") ?: -1L
            fileByteLength = queryLong(id, "file_byte_length") ?: -1L
            encoding = Audio.Encoding.valueOf(queryString(id, "encoding") ?: "UNKNOWN")
            channels = queryInt(id, "channels") ?: -1
            sampleRate = queryInt(id, "sample_rate") ?: -1
            bitDepth = queryInt(id, "bit_depth") ?: -1
            bitRate = queryInt(id, "bitrate") ?: -1
            gain = queryDouble(id, "gain") ?: 0.0
            signed = queryBoolean(id, "signed") ?: true
        }
    }

    fun contains(id: Long): Boolean = super.exists(TABLE_NAME, id)
    fun queryLong(id: Long, column: String): Long? = super.queryLong(TABLE_NAME, id, column)
    fun queryInt(id: Long, column: String): Int? = super.queryInt(TABLE_NAME, id, column)
    fun queryDouble(id: Long, column: String): Double? = super.queryDouble(TABLE_NAME, id, column)
    fun queryBoolean(id: Long, column: String): Boolean? = super.queryBoolean(TABLE_NAME, id, column)
    fun queryString(id: Long, column: String): String? = super.queryString(TABLE_NAME, id, column)
}