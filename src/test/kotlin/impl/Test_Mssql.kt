package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.mssql.MssqlSerializer
import com.urosjarc.dbmessiah.impl.mssql.MssqlService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.mssql_schema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Mssql : Test_Contract {

    companion object {
        val ser = MssqlSerializer(
            schemas = listOf(mssql_schema),
            globalSerializers = BasicTS.mssql + JavaTimeTS.mssql + KotlinxTimeTS.mssql + listOf(Serializers.mssql, Serializers.int)
        )
        val service = MssqlService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:sqlserver://localhost:1433;encrypt=false;"
                this["username"] = "sa"
                this["password"] = "Root_root1"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(mssql_schema)
                it.table.drop<Child>()
                it.table.drop<Parent>()
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
