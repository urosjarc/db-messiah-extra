package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.derby.DerbySerializer
import com.urosjarc.dbmessiah.impl.derby.DerbyService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.derby_schema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Derby : Test_Contract {

    companion object {
        val ser = DerbySerializer(
            schemas = listOf(derby_schema),
            globalSerializers = BasicTS.derby + JavaTimeTS.derby + KotlinxTimeTS.derby + listOf(Serializers.derby, Serializers.int)
        )
        val service = DerbyService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:derby:memory:db;create=true"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(derby_schema)
                it.table.drop<Child>(throws = false)
                it.table.drop<Parent>(throws = false)
                it.table.create<Parent>()
                it.table.create<Child>()
            }
        }
    }

    @Test
    override fun `test serializers`() {
        service.autocommit {
            val parent = Parent(col = "col")
            val child = Child(fk = parent.id, col = "col")

            it.row.insert(parent)
            it.row.insert(child)

            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }

}
