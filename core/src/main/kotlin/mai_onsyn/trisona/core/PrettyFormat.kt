package mai_onsyn.trisona.core

fun format(string: String): String {
    val sb = StringBuilder()
    var depth = 0
    var warpped = false
    string.toCharArray().forEach {
        when (it) {
            '{', '(', '[' -> {
                depth++
                sb.append(it)
                    .append('\n')
                warpped = true
            }
            '}', ')', ']' -> {
                depth--
                sb.append('\n')
                repeat(depth * 4) { sb.append(' ') }
                sb.append(it)
                warpped = true
            }
            ',' -> {
                sb.append(it).append('\n')
                warpped = true
            }
            ' ' -> {
                if (!warpped) {
                    sb.append(it)
                }
            }
            else -> {
                if (warpped) {
                    repeat(depth * 4) {
                        sb.append(' ')
                    }
                    warpped = false
                }
                sb.append(it)
            }
        }
    }
    return sb.toString()
}

//fun format(any: Any): String = format(any.toString())
fun format(any: Any?): String = format(any?.toString()?: "null")