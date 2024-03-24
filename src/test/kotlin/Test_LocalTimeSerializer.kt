import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import utils.json
import java.time.LocalTime
import kotlin.test.assertEquals

class Test_LocalTimeSerializer {
    @Test
    fun `test serialization`() {
        val instant = LocalTime.now()

        /** Encoding */
        val instantJson = json.encodeToString(instant)
        assertEquals("\"$instant\"", instantJson)

        /** Decoding */
        val instant_new = json.decodeFromString<LocalTime>(instantJson)
        assertEquals(instant, instant_new)
    }
}
