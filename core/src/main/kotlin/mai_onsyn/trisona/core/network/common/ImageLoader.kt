package mai_onsyn.trisona.core.network.common

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mai_onsyn.trisona.Global
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import javax.imageio.ImageIO

object ImageLoader {
    private val log: Logger = LoggerFactory.getLogger(ImageLoader::class.java)

    private suspend fun fromURL(url: String): BufferedImage? {
        try {
            val response = client.get(url) {
                header("User-Agent", Global.USER_AGENT)
            }
            return withContext(Dispatchers.IO) {
                ImageIO.read(ByteArrayInputStream(response.bodyAsBytes()))
            }
        } catch (e: Exception) {
            log.error("Failed to load image from URL: {}", url, e)
            return null
        }
    }

    private fun fromPath(path: String): BufferedImage? {
        try {
            val read = ImageIO.read(FileInputStream(path))
            return read
        } catch (e: IOException) {
            log.error("Failed to load image from path: {}", path, e)
            return null
        }
    }

    fun fromSync(url: String): BufferedImage? {
        log.debug("Loading image from $url")
        return if (url.startsWith("http://") || url.startsWith("https://"))
            runBlocking { fromURL(url) }
        else fromPath(url)
    }
}