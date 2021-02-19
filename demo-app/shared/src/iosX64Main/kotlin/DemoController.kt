
class DemoController {

    val key = ByteArray(32) { it.toByte() } // A completely unsecure key!

    private fun Long.toBytes(): ByteArray {
        val result = ByteArray(8)
        var l = this
        for (i in 7 downTo 0) {
            result[i] = (l and 0xFF).toByte()
            l = l ushr 8
        }
        return result
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun encrypt(nonce: Long, text: String): String {
        val plainText = text.encodeToByteArray()
        val nonceBytes = ByteArray(4) + nonce.toBytes()
        val cipherTextAndTag = Crypto.ChachaPoly.encrypt(key, nonceBytes, ByteArray(0), plainText)
        return cipherTextAndTag.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
    }

}