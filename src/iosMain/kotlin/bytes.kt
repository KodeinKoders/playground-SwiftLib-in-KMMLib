fun Long.toBytes(): ByteArray {
    val result = ByteArray(8)
    var l = this
    for (i in 7 downTo 0) {
        result[i] = (l and 0xFF).toByte()
        l = l ushr 8
    }
    return result
}

fun ByteArray.toLong(): Long {
    var result: Long = 0
    for (i in 0..7) {
        result = result shl 8
        result = result or (this[i].toInt() and 0xFF).toLong()
    }
    return result
}
