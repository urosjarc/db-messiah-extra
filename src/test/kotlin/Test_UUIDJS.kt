import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import utils.Parent
import utils.UId
import utils.json
import kotlin.test.assertEquals

class Test_UUIDJS {
    @Test
    fun `test id serialization`() {
        val id = UId<Parent>()

        /** Encoding */
        val id_json = json.encodeToString(id)
        assertEquals("\"${id.value}\"", id_json)

        /** Decoding */
        val id_new = json.decodeFromString<UId<Parent>>(id_json)
        assertEquals(id, id_new)
    }

    @Test
    fun `test parent serialization`() {
        val parent = Parent(col = "col")

        /** Encoding */
        val parent_json = json.encodeToString(parent)
        assertEquals("{\"id\":\"${parent.id}\",\"col\":\"col\"}", parent_json)

        /** Decoding */
        val parent_new = json.decodeFromString<Parent>(parent_json)
        assertEquals(parent, parent_new)
    }
}
