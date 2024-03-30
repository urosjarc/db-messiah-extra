import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import utils.json
import java.time.Instant
import kotlin.test.assertEquals

class Test_InstantJS {
    @Test
    fun `test serialization`() {
        val instant = Instant.now()

        /** Encoding */
        val instantJson = json.encodeToString(instant)
        assertEquals("\"$instant\"", instantJson)

        /** Decoding */
        val instant_new = json.decodeFromString<Instant>(instantJson)
        assertEquals(instant, instant_new)
    }
}
