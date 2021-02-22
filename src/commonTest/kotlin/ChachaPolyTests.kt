package org.demo.crypto

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChachaPolyTests {
    @Test fun `encrypt & decrypt simple text`() {

        val key = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef".fromHex()
        val nonce = "00000000fedcba9876543210".fromHex()

        val plaintext = "Hello, world!".encodeToByteArray()
        val aad = ByteArray(0)

        val ciphertextAndTag = ChachaPoly.encrypt(key, nonce, aad, plaintext)

        assertEquals("295de91218883c99efed30a0ea56db82d1f05aa5350f49f32f923ce345", ciphertextAndTag.toHex())

        val otherPlaintext = ChachaPoly.decrypt(key, nonce, aad, ciphertextAndTag)

        assertTrue(plaintext.contentEquals(otherPlaintext))
    }

}
