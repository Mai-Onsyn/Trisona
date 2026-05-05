package mai_onsn.trisona.ui.util

fun formatMillisToTime(millis: Float): String {
    return formatMillisToTime(millis.toLong())
}

fun formatMillisToTime(millis: Int): String {
    return formatMillisToTime(millis.toLong())
}

fun formatMillisToTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds)
    else String.format("%02d:%02d", minutes, seconds)
}