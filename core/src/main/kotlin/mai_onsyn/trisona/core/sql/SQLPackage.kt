package mai_onsyn.trisona.core.sql

data class SQLPackage(
    val musicSQL: MusicSQL,
    val audioSQL: AudioSQL,
    val artistSQL: ArtistSQL,
    val albumSQL: AlbumSQL,
    val playListSQL: PlayListSQL
)