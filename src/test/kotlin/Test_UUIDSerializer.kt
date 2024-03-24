import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import utils.UId
import utils.Parent
import kotlin.test.assertEquals

class test_UUIDSerializer {
    @Test
    fun `test id serialization`() {
        val id = UId<Parent>()

        /** Encoding */
        val id_json = Json.encodeToString(id)
        assertEquals("\"${id.value}\"", id_json)

        /** Decoding */
        val id_new = Json.decodeFromString<UId<Parent>>(id_json)
        assertEquals(id, id_new)
    }

    @Test
    fun `test parent serialization`() {
        val parent = Parent(col = "col")

        /** Encoding */
        val parent_json = Json.encodeToString(parent)
        assertEquals("{\"id\":\"${parent.id}\",\"col\":\"col\"}", parent_json)

        /** Decoding */
        val parent_new = Json.decodeFromString<Parent>(parent_json)
        assertEquals(parent, parent_new)
    }
}
