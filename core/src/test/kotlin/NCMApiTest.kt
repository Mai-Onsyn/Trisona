import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import mai_onsyn.trisona.core.data.Music
import mai_onsyn.trisona.core.network.ncme.base.API
import mai_onsyn.trisona.core.network.ncme.base.NCMApi
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request

fun main() {
    val ref = API.music.getNetEaseUrl(1342511614L, Music.MusicQuality.STANDARD)
    API.client.close()

    println(ref.url)
    println(ref.info)
}