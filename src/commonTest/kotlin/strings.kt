@file:OptIn(ExperimentalUnsignedTypes::class)
package org.demo.crypto


fun ByteArray.toHex(): String = buildString {
    this@toHex.forEach {
        append(it.toUByte().toString(radix = 16).padStart(2, '0'))
    }
}

fun String.fromHex(): ByteArray {
    require(length % 2 == 0)
    val array = ByteArray(length / 2)
    chunked(2).forEachIndexed { i, s ->
        array[i] = s.toUByte(radix = 16).toByte()
    }
    return array
}
