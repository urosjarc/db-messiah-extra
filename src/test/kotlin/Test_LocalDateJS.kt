import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import utils.json
import java.time.LocalDate
import kotlin.test.assertEquals

class Test_LocalDateJS {
    @Test
    fun `test serialization`() {
        val instant = LocalDate.now()

        /** Encoding */
        val instantJson = json.encodeToString(instant)
        assertEquals("\"$instant\"", instantJson)

        /** Decoding */
        val instant_new = json.decodeFromString<LocalDate>(instantJson)
        assertEquals(instant, instant_new)
    }
}
