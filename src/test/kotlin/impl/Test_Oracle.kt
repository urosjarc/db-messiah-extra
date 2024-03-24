package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.oracle.OracleSerializer
import com.urosjarc.dbmessiah.impl.oracle.OracleService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.oracle_schema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Oracle : Test_Contract {

    companion object {
        val ser = OracleSerializer(
            schemas = listOf(oracle_schema),
            globalSerializers = BasicTS.oracle + JavaTimeTS.oracle + KotlinxTimeTS.oracle + listOf(Serializers.oracle, Serializers.int)
        )
        val service = OracleService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:oracle:thin:@localhost:1521:XE"
                this["username"] = "system"
                this["password"] = "root"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.table.drop<Child>(throws = false)
                it.table.drop<Parent>(throws = false)
                it.table.create<Parent>(throws = false)
                it.table.create<Child>(throws = false)
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

            println(child)

            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }

}
