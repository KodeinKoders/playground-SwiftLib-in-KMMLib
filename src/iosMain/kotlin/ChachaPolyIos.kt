package org.demo.crypto

import kotlinx.cinterop.autoreleasepool
import platform.Foundation.NSData
import swift.chachaPoly.*

actual object ChachaPoly {

    private fun DataResult.unwrap(): NSData {
        this.failure()?.let { throw ChachaPolyException(it.description ?: "unknown error") }
        return success() ?: error("Invalid result")
    }

    actual fun encrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, plaintext: ByteArray): ByteArray {
        autoreleasepool {
            val keyData = key.toData()
            val nonceData = nonce.toData()
            val aadData = aad.toData()
            val plaintextData = plaintext.toData()

            return SwiftChachaPoly.encryptWithKey(keyData, nonceData, aadData, plaintextData).unwrap().toByteArray()
        }
    }

    actual fun decrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, ciphertextAndTag: ByteArray): ByteArray {
        require(ciphertextAndTag.size >= 16) { "ciphertextAndTag should be at least 16 bytes long" }

        autoreleasepool {
            val keyData = key.toData()
            val nonceData = nonce.toData()
            val aadData = aad.toData()
            val ciphertextAndTagData = ciphertextAndTag.toData()

            return SwiftChachaPoly.decryptWithKey(keyData, nonceData, aadData, ciphertextAndTagData).unwrap().toByteArray()
        }
    }

}
