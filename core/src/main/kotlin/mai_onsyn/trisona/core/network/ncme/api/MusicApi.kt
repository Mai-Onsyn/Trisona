package mai_onsyn.trisona.core.network.ncme.api

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONPath
import kotlinx.coroutines.runBlocking
import mai_onsyn.trisona.core.data.Album
import mai_onsyn.trisona.core.data.MusicQuality
import mai_onsyn.trisona.core.log
import mai_onsyn.trisona.core.message.Artist
import mai_onsyn.trisona.core.message.AudioMessage
import mai_onsyn.trisona.core.message.MusicMessage
import mai_onsyn.trisona.core.network.ncme.base.NCMApiService

class MusicApi(val client: NCMApiService) {
    data class IdUrlResponse(
        val url: String,
        val info: MusicMessage?
    )
    fun getNetEaseUrl(id: Long, level: MusicQuality): IdUrlResponse = runBlocking {
        val params = mapOf(
            "id" to id.toString(),
            "level" to level.id,
            "unlock" to "false"
        )
        val json = client.requestJson("song/url/v1", params)

        val url = json.optString("$.data[0].url").orEmpty()
        if (url.isEmpty()) return@runBlocking IdUrlResponse("", null)

        val mMsg = MusicMessage()
        mMsg.id = id
        mMsg.audioPath.setNetWorkUrl(url)
        mMsg.duration = json.optString("$.data[0].time")?.toIntOrNull()?: -1
        mMsg.enableQuality(level)

        val aMsg = AudioMessage()
        aMsg.encoding = when(
            json.optString("$.data[0].encodeType").orEmpty()
        ) {
            "mp3" -> AudioMessage.Encoding.MP3
            "flac" -> AudioMessage.Encoding.FLAC
            else -> {
                log.warn("Unknown encoding: ${json.optString("$.data[0].encodeType")}")
                AudioMessage.Encoding.UNKNOWN
            }
        }
        aMsg.bitRate = json.optString("$.data[0].br")?.toIntOrNull()?: -1
        aMsg.fileByteLength = json.optString("$.data[0].size")?.toLongOrNull()?: -1L
        aMsg.gain = json.optString("$.data[0].gain")?.toDoubleOrNull()?: 0.0
        aMsg.sampleRate = json.optString("$.data[0].sr")?.toIntOrNull()?: -1

        mMsg.setaMsgNetwork(aMsg)
        IdUrlResponse(url, mMsg)
    }

    fun getNetEaseMusicDetail(id: Long, album: Album): MusicMessage = runBlocking {
        val json = client.requestJson("/song/detail", mapOf("ids" to id.toString()))
        val mMsg = MusicMessage()
        val song = json.optJSONObject("$.songs[0]")?: return@runBlocking mMsg
        val aMsgMap = mutableMapOf<MusicQuality, AudioMessage>()

        mapOf(
            "hr" to MusicQuality.HI_RES,
            "sq" to MusicQuality.LOSSLESS,
            "h"  to MusicQuality.EX_HIGH,
            "m"  to MusicQuality.HIGHER,
            "l"  to MusicQuality.STANDARD
        ).forEach { (apiKey, qualityEnum) ->
            song.getJSONObject(apiKey)?.let {
                aMsgMap[qualityEnum] = AudioMessage().apply {
                    bitRate = it.getString("br")?.toIntOrNull()?: -1
                    fileByteLength = it.getString("size")?.toLongOrNull()?: -1
                    sampleRate = it.getString("sr")?.toIntOrNull()?: -1
                }
                mMsg.enableQuality(qualityEnum)
            }
        }
        aMsgMap.forEach { (quality, aMsg) -> mMsg.getAudioMessage(quality).network = aMsg }

        mMsg.apply {
            this.id = id
            title = song.getString("name").orEmpty()
            artists = song.getJSONArray("ar")?.mapNotNull {
                val obj = it as? JSONObject?: return@mapNotNull null
                Artist(
                    obj.getLongValue("id", -1L),
                    obj.getString("name").orEmpty(),
                    obj.getList("alias", String::class.java)?: emptyList()
                )
            }?: emptyList()
            alias = song.getJSONArray("alia")?.mapNotNull {
                it?.toString() ?: return@mapNotNull null
            } ?: emptyList()
            popularity = song.getString("pop")?.toDoubleOrNull()?: 0.0
            fee = (song.optString("fee")?.toIntOrNull()?: 0) == 0
            albumID = song.optString("$.al.id")?.toLongOrNull()?: -1L
            duration = song.getString("dt")?.toIntOrNull()?: 0
            currentQuality = json.optString("$.privileges[0].fl")
                ?.let { MusicQuality.byId(it)?: MusicQuality.STANDARD }
        }

        album.apply {
            setId(mMsg.albumID)
            setName(song.optString("$.al.name").orEmpty())
            setPicUrlNet(song.optString("$.al.picUrl").orEmpty())
            add(id)
        }
//        log.debug(json.toJSONString(JSONWriter.Feature.PrettyFormat))
        return@runBlocking mMsg
    }
}

fun JSONObject.opt(key: String): Any? {
    return JSONPath.eval(this, key)
}

fun JSONObject.optString(key: String): String? {
    return JSONPath.eval(this, key)?.toString()
}

fun JSONObject.optJSONObject(key: String): JSONObject? {
    return JSONPath.eval(this, key) as? JSONObject
}

fun JSONObject.optJSONArray(key: String): JSONArray? {
    return JSONPath.eval(this, key) as? JSONArray
}