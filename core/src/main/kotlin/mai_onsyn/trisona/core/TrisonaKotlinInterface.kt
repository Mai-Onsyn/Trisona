package mai_onsyn.trisona.core

import mai_onsyn.trisona.core.data.Music
import mai_onsyn.trisona.core.decoder.AudioDetector
import mai_onsyn.trisona.core.message.MusicMessage
import mai_onsyn.trisona.core.message.PlayListMessage
import mai_onsyn.trisona.core.message.UniversalPath
import mai_onsyn.trisona.core.play.PlayList
import mai_onsyn.trisona.core.play.TrisonaPlayer
import java.io.File

object TrisonaKotlinInterface {
    val player = TrisonaPlayer("desktop")

    init {
        val m1 = MusicMessage()
        m1.audioPath = UniversalPath(null, "D:\\CloudMusic\\monet - GHOST×GRADUATION.flac")

        val m2 = MusicMessage()
        m2.audioPath = UniversalPath(null, "D:\\Users\\Desktop\\Files\\voice\\Adio\\monet - ナグルファルの船上にて.wav")

        val m3 = MusicMessage()
        m3.audioPath = UniversalPath(null, "D:\\CloudMusic\\雪桜草 - 渚 ~君と目指した高み、愿いが叶う场所~.mp3")

        player.setOnMusicStart { music ->
            val mmsg = AudioDetector.detectMusic(File(music.info.audioPath.path.url))
//            println(mmsg)
        }

        val playList = PlayList(PlayListMessage())
        playList.add(Music(m1))
        playList.add(Music(m2))
        playList.add(Music(m3))

        player.setPlayList(playList)
        player.volume = 20
    }
}