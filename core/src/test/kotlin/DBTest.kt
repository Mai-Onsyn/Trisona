import mai_onsyn.trisona.core.TrisonaKotlinInterface.SQL_INSTANCE
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.artistSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.audioSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.musicSQL
import mai_onsyn.trisona.core.format
import mai_onsyn.trisona.core.log

fun main() {
    val mmsg = musicSQL.query(1342511614L, audioSQL, artistSQL)
    log.info(format(mmsg))
    val albumMsg = mmsg?.let { albumSQL.query(it.albumID) }
    log.info(format(albumMsg))

    SQL_INSTANCE.printAllTables()
    SQL_INSTANCE.connection.close()

//    Thread.sleep(1000)
}