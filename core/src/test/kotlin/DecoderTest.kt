import mai_onsyn.trisona.core.play.JVMAudioPlayer
import mai_onsyn.trisona.core.message.Music
import mai_onsyn.trisona.core.message.UniversalPath

val player = JVMAudioPlayer()
fun main() {

    player.setOnMusicEnd { println("Play end") }

    val m1 = Music()
    m1.audioPath = UniversalPath(null, "D:\\CloudMusic\\monet - GHOST×GRADUATION.flac")

    val m2 = Music()
    m2.audioPath = UniversalPath(null, "D:\\Users\\Desktop\\Files\\voice\\Adio\\monet - ナグルファルの船上にて.wav")

    val m3 = Music()
    m3.audioPath = UniversalPath(null, "D:\\CloudMusic\\雪桜草 - 渚 ~君と目指した高み、愿いが叶う场所~.mp3")

    val m4 = Music()
    m4.audioPath = UniversalPath(null, "D:/Users/Desktop/Files/Videos/usotuki.wav")

    player.play()
    player.setMusic(m4)
    println("playing")
//    Thread.sleep(5000)
//
//    seek(20000)
//    seek(999999999)
//    seek(200000)
//    switch(m1)
//    seek(100000)
//    pause()
//    resume()
//    switch(m2)
//    seek(140000)
//    seek(280000)

//    val fileInputStream = FileInputStream("D:\\CloudMusic\\雪桜草 - 渚 ~君と目指した高み、愿いが叶う场所~.mp3")
//    Mp3Decoder.skipID3Tag(fileInputStream)
//    fileInputStream.close()

    while (true) {}
}

fun seek(time: Int) {
    player.seek(time)
    println("seeked to $time")
    Thread.sleep(5000)
}

fun switch(msg: Music) {
    player.setMusic(msg)
    println("switched to $msg")
    Thread.sleep(5000)
}

fun pause() {
    player.pause()
    println("paused")
    Thread.sleep(5000)
}

fun resume() {
    player.play()
    println("resumed")
    Thread.sleep(5000)
}