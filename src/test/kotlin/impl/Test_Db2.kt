package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.db2.Db2Serializer
import com.urosjarc.dbmessiah.impl.db2.Db2Service
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.*
import utils.Serializers
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Db2 : Test_Contract {

    companion object {
        val ser = Db2Serializer(
            schemas = listOf(db2_schema),
            globalSerializers = BasicTS.db2 + JavaTimeTS.db2 + KotlinxTimeTS.db2 + listOf(Serializers.db2, Serializers.int)
        )
        val service = Db2Service(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:db2://localhost:50000/main"
                this["username"] = "db2inst1"
                this["password"] = "root"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(db2_schema, throws = false)
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
