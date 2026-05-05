package mai_onsyn.trisona.core

import mai_onsyn.trisona.core.data.Album
import mai_onsyn.trisona.core.data.Date
import mai_onsyn.trisona.core.data.MusicQuality
import mai_onsyn.trisona.core.decoder.AudioDetector
import mai_onsyn.trisona.core.message.Music
import mai_onsyn.trisona.core.message.PlayListInfo
import mai_onsyn.trisona.core.message.UniversalPath
import mai_onsyn.trisona.core.play.PlayList
import mai_onsyn.trisona.core.play.TrisonaPlayer
import mai_onsyn.trisona.core.sql.AlbumSQL
import mai_onsyn.trisona.core.sql.ArtistSQL
import mai_onsyn.trisona.core.sql.AudioSQL
import mai_onsyn.trisona.core.sql.MusicSQL
import mai_onsyn.trisona.core.sql.PlayListSQL
import mai_onsyn.trisona.core.sql.SQLInstance
import mai_onsyn.trisona.core.sql.SQLPackage
import java.io.File

object TrisonaKotlinInterface {
    val player = TrisonaPlayer("desktop")

    val SQL_INSTANCE = SQLInstance("local_info")
    val musicSQL = MusicSQL(SQL_INSTANCE)
    val audioSQL = AudioSQL(SQL_INSTANCE)
    val artistSQL = ArtistSQL(SQL_INSTANCE)
    val albumSQL = AlbumSQL(SQL_INSTANCE)
    val playListSQL = PlayListSQL(SQL_INSTANCE)
    val sql = SQLPackage(
        musicSQL = musicSQL,
        audioSQL = audioSQL,
        artistSQL = artistSQL,
        albumSQL = albumSQL,
        playListSQL = playListSQL
    )

    var testPlayList: PlayList? = null

    init {
//        val m1 = Music()
//        m1.audioPath = UniversalPath(null, "D:\\CloudMusic\\monet - GHOST×GRADUATION.flac")
//        m1.enableQuality(MusicQuality.NATIVE)
//        m1.albumID = -208455816664149185L
//        m1.id = -4794498101323629425
//
//        val m2 = Music()
//        m2.audioPath = UniversalPath(null, "D:\\Users\\Desktop\\Files\\voice\\Adio\\monet - ナグルファルの船上にて.wav")
//        m2.enableQuality(MusicQuality.NATIVE)
//        m2.id = -4740286149887431473
//
//        val m3 = Music()
//        m3.audioPath = UniversalPath(null, "D:\\CloudMusic\\雪桜草 - 渚 ~君と目指した高み、愿いが叶う场所~.mp3")
//        m3.enableQuality(MusicQuality.NATIVE)

        player.setOnMusicStart { music ->
            log.info("Music Start: {}", music.title)
            val album = Album()
            val mmsg = AudioDetector.detectMusic(File(music.audioPath.path.url), album)
//            albumSQL.storage(album)
//            mmsg?.let { musicSQL.storage(mmsg, sql) }
//            SQL_INSTANCE.printAllTables()
//            log.debug("Detected audio file header: {}", mmsg)
        }

        val loadMusics = AudioDetector.loadMusics(File("D:\\CloudMusic"), sql)
//        val loadMusics = File("E:\\Files\\music").listFiles()?.mapNotNull {
//            AudioDetector.detectMusic(it, Album())
//        }!!
//        loadMusics.forEach {
//            println(it)
//        }

        testPlayList = PlayList(PlayListInfo().apply {
            name = "NetEast Download"
            creator = "Trisona"
            createDate = Date(System.currentTimeMillis())
            coverPath = UniversalPath(null, "D:\\Users\\Desktop\\image_142.png")
        })
        testPlayList?.addAll(loadMusics)
//        testPlayList?.let { playListSQL.storage(it) }
//        testPlayList = playListSQL.query("Test PlayList", sql)
//        testPlayList = playListSQL.query("NetEast Download", sql)

        player.setPlayList(testPlayList)
        player.volume = 20
    }
}