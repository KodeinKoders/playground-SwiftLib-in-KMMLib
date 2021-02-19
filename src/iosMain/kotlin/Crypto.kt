import kotlinx.cinterop.autoreleasepool
import platform.Foundation.NSData
import swift.crypto_kit.DataResult
import swift.crypto_kit.SwiftCryptoKit

object Crypto {

    class CryptoException(message: String) : Exception(message)

    fun DataResult.unwrap(): NSData {
        this.failure()?.let { throw CryptoException(it.description ?: "unknown error") }
        return success() ?: error("Invalid result")
    }

    object ChachaPoly {

        fun encrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, plaintext: ByteArray): ByteArray {
            autoreleasepool {
                val keyData = key.toData()
                val nonceData = nonce.toData()
                val aadData = aad.toData()
                val plaintextData = plaintext.toData()

                return SwiftCryptoKit.chachapoly_encryptWithKey(keyData, nonceData, aadData, plaintextData).unwrap().toByteArray()
            }
        }

        fun decrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, ciphertextAndTag: ByteArray): ByteArray {
            if (ciphertextAndTag.size < 16) { throw CryptoException("ciphertextAndTag should be at least 16 bytes long") }

            autoreleasepool {
                val keyData = key.toData()
                val nonceData = nonce.toData()
                val aadData = aad.toData()
                val ciphertextAndTagData = ciphertextAndTag.toData()

                return SwiftCryptoKit.chachapoly_decryptWithKey(keyData, nonceData, aadData, ciphertextAndTagData).unwrap().toByteArray()
            }
        }
    }

}
