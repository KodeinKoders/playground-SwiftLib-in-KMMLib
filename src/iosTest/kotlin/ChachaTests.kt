import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class ChachaTests {

    @Test fun `encrypt & decrypt simple text`() {

        val key = Random.nextBytes(32)
        val nonce = Random.nextBytes(12)

        val plaintext = "Hello, world!".encodeToByteArray()
        val aad = ByteArray(0)

        val ciphertextAndTag = Crypto.ChachaPoly.encrypt(key, nonce, aad, plaintext)

        val otherPlaintext = Crypto.ChachaPoly.decrypt(key, nonce, aad, ciphertextAndTag)

        assertTrue(plaintext.contentEquals(otherPlaintext))
    }

}
