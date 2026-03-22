import mai_onsyn.trisona.core.decoder.AudioDetector
import java.io.FileInputStream

fun main() {
    val stream = FileInputStream("D:\\CloudMusic\\小春めう - voice.ogg")

    try {

        val detectionResult = AudioDetector.detectFormatInStream(stream)

        println(detectionResult.encoding())

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        stream.close()
    }

}