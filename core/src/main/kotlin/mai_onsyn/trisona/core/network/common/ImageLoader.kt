package mai_onsyn.trisona.core.network.common

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.runBlocking
import mai_onsyn.trisona.Global
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import javax.imageio.ImageIO

object ImageLoader {
    private val log: Logger = LoggerFactory.getLogger(ImageLoader::class.java)

    private suspend fun fromURL(url: String): BufferedImage? {
        try {
            log.debug("Loading image from $url")
            val response = client.get(url) {
                header("User-Agent", Global.USER_AGENT)
            }
            return ImageIO.read(ByteArrayInputStream(response.bodyAsBytes()))
        } catch (e: Exception) {
            log.error("Failed to load image from URL: {}", url, e)
            return null
        }
    }

    fun fromURLSync(url: String): BufferedImage? = runBlocking { fromURL(url) }
}