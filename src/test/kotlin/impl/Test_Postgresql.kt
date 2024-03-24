package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.postgresql.PgSerializer
import com.urosjarc.dbmessiah.impl.postgresql.PgService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class Test_Postgresql : Test_Contract {

    companion object {
        val ser = PgSerializer(
            schemas = listOf(pg_schema),
            globalSerializers = BasicTS.postgresql + JavaTimeTS.postgresql + KotlinxTimeTS.postgresql + listOf(Serializers.postgresql, Serializers.int)
        )
        val service = PgService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:postgresql://localhost:5432/public"
                this["username"] = "root"
                this["password"] = "root"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(pg_schema)
                it.table.drop<Child>()
                it.table.drop<AutoChild>()
                it.table.drop<Parent>()
                it.table.drop<AutoParent>(throws = false)
                it.table.create<Parent>()
                it.table.create<AutoParent>()
                it.table.create<Child>()
                it.table.create<AutoChild>()
            }
        }
    }


    @Test
    override fun `test serializers`() {
        service.autocommit {
            val parent = Parent(col = "col")
            val autoParent = AutoParent(id = null, col = "col")
            val child = Child(fk = parent.id, col = "col")
            val autoChild = AutoChild(col = "col")

            it.row.insert(autoParent)
            it.row.insert(parent)
            it.row.insert(child)
            it.row.insert(autoChild)

            assertNotNull(actual = autoParent.id)
            assertNotNull(actual = autoChild.id)
            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }
}
