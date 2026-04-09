package mai_onsyn.trisona.core

import mai_onsyn.trisona.core.data.Album
import mai_onsyn.trisona.core.data.MusicQuality
import mai_onsyn.trisona.core.decoder.AudioDetector
import mai_onsyn.trisona.core.message.MusicMessage
import mai_onsyn.trisona.core.message.PlayListMessage
import mai_onsyn.trisona.core.message.UniversalPath
import mai_onsyn.trisona.core.play.PlayList
import mai_onsyn.trisona.core.play.TrisonaPlayer
import mai_onsyn.trisona.core.sql.AlbumSQL
import mai_onsyn.trisona.core.sql.ArtistSQL
import mai_onsyn.trisona.core.sql.AudioSQL
import mai_onsyn.trisona.core.sql.MusicSQL
import mai_onsyn.trisona.core.sql.SQLInstance
import java.io.File

object TrisonaKotlinInterface {
    val player = TrisonaPlayer("desktop")

    val SQL_INSTANCE = SQLInstance("local_info")
    val musicSQL = MusicSQL(SQL_INSTANCE)
    val audioSQL = AudioSQL(SQL_INSTANCE)
    val artistSQL = ArtistSQL(SQL_INSTANCE)
    val albumSQL = AlbumSQL(SQL_INSTANCE)

    init {
        val m1 = MusicMessage()
        m1.audioPath = UniversalPath(null, "D:\\CloudMusic\\monet - GHOST×GRADUATION.flac")
        m1.enableQuality(MusicQuality.NATIVE)

        val m2 = MusicMessage()
        m2.audioPath = UniversalPath(null, "D:\\Users\\Desktop\\Files\\voice\\Adio\\monet - ナグルファルの船上にて.wav")
        m2.enableQuality(MusicQuality.NATIVE)

        val m3 = MusicMessage()
        m3.audioPath = UniversalPath(null, "D:\\CloudMusic\\雪桜草 - 渚 ~君と目指した高み、愿いが叶う场所~.mp3")
        m3.enableQuality(MusicQuality.NATIVE)

        player.setOnMusicStart { music ->
            val album = Album()
            val mmsg = AudioDetector.detectMusic(File(music.audioPath.path.url), album)
            albumSQL.storage(album)
            mmsg?.let { musicSQL.storage(mmsg, audioSQL, artistSQL) }
            SQL_INSTANCE.printAllTables()
//            log.debug("Detected audio file header: {}", mmsg)
        }

        val playList = PlayList(PlayListMessage())
        playList.add(m1)
        playList.add(m2)
        playList.add(m3)

        player.setPlayList(playList)
        player.volume = 20
    }
}