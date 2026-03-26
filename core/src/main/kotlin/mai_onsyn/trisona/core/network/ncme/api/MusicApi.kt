package mai_onsyn.trisona.core.network.ncme.api

import com.alibaba.fastjson2.JSONPath
import kotlinx.coroutines.runBlocking
import mai_onsyn.trisona.core.data.Music
import mai_onsyn.trisona.core.message.AudioMessage
import mai_onsyn.trisona.core.message.MusicMessage
import mai_onsyn.trisona.core.network.ncme.base.NCMApiService

class MusicApi(val client: NCMApiService) {
    data class IdUrlResponse(
        val url: String,
        val info: MusicMessage?
    )
    fun getNetEaseUrl(id: Long, level: Music.MusicQuality): IdUrlResponse = runBlocking {
        val params = mapOf(
            "id" to id.toString(),
            "level" to level.id,
            "unlock" to "false"
        )
        val json = client.requestJson("song/url/v1", params)

        val url = JSONPath.eval(json, "$.data[0].url")?.toString() ?: ""
        if (url.isEmpty()) return@runBlocking IdUrlResponse("", null)

        val mMsg = MusicMessage()
        mMsg.audioPath.setNetWorkUrl(url)
        mMsg.duration = JSONPath.eval(json, "$.data[0].time")?.toString()?.toInt() ?: -1

        val aMsg = AudioMessage()
        aMsg.encoding = when(
            JSONPath.eval(json, "$.data[0].encodeType")?.toString() ?: ""
        ) {
            "mp3" -> AudioMessage.Encoding.MP3
            "flac" -> AudioMessage.Encoding.FLAC
            else -> AudioMessage.Encoding.UNKNOWN
        }
        aMsg.bitRate = JSONPath.eval(json, "$.data[0].br")?.toString()?.toInt() ?: -1
        aMsg.fileByteLength = JSONPath.eval(json, "$.data[0].size")?.toString()?.toLong() ?: -1L
        aMsg.gain = JSONPath.eval(json, "$.data[0].gain")?.toString()?.toDouble() ?: 0.0
        aMsg.sampleRate = JSONPath.eval(json, "$.data[0].sr")?.toString()?.toInt() ?: -1

        mMsg.setaMsgNetwork(aMsg)
        IdUrlResponse(url, mMsg)
    }
}