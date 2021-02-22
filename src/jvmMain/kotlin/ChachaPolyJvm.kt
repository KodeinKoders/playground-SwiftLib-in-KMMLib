package org.demo.crypto

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


actual object ChachaPoly {

    private fun getCipher() = Cipher.getInstance("ChaCha20-Poly1305") // JVM 11+
        ?: Cipher.getInstance("ChaCha20/Poly1305/NoPadding") // Android 28+
        ?: error("Could not find ChachaPoly cipher. Are you running JVM 11+ or Android 28+?")

    private inline fun <R> wrap(block: () -> R): R =
        try {
            block()
        } catch (ex: Throwable) {
            throw ChachaPolyException(ex.message ?: "Unknown error")
        }

    actual fun encrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, plaintext: ByteArray): ByteArray = wrap {
        val secretKey = SecretKeySpec(key, "Chacha20")
        val initializationVector = IvParameterSpec(nonce)
        return with(getCipher()) {
            init(Cipher.ENCRYPT_MODE, secretKey, initializationVector)
            if (aad.isNotEmpty()) updateAAD(aad)
            doFinal(plaintext)
        }
    }

    actual fun decrypt(key: ByteArray, nonce: ByteArray, aad: ByteArray, ciphertextAndTag: ByteArray): ByteArray = wrap {
        val secretKey = SecretKeySpec(key, "Chacha20")
        val initializationVector = IvParameterSpec(nonce)
        return with(getCipher()) {
            init(Cipher.DECRYPT_MODE, secretKey, initializationVector)
            if (aad.isNotEmpty()) updateAAD(aad)
            doFinal(ciphertextAndTag)
        }
    }
}
