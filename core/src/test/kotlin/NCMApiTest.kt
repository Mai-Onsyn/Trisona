import mai_onsyn.trisona.core.TrisonaKotlinInterface.SQL_INSTANCE
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.artistSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.audioSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.musicSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.sql
import mai_onsyn.trisona.core.data.Album
import mai_onsyn.trisona.core.data.MusicQuality
import mai_onsyn.trisona.core.network.common.API
import java.io.PrintStream
import java.nio.charset.StandardCharsets

fun main() {
    System.setOut(PrintStream(System.`out`, true, StandardCharsets.UTF_8.name()))
    val album = Album()
    val ref1 = API.music.getNetEaseUrl(1342511614L, MusicQuality.EX_HIGH)
    val ref2 = API.music.getNetEaseMusicDetail(1342511614L, album)
    API.client.close()

    albumSQL.storage(album)
    musicSQL.storage(ref1.info!!, sql)
    musicSQL.storage(ref2, sql)

//    Thread.sleep(3000)
    SQL_INSTANCE.printAllTables()
    SQL_INSTANCE.connection.close()

//    log.info("album: ${format(album)}")
//    log.info("ref: ${format(ref)}")
}