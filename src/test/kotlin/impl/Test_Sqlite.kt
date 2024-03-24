package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.sqlite.SqliteSerializer
import com.urosjarc.dbmessiah.impl.sqlite.SqliteService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.domain_tables
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Sqlite : Test_Contract {

    companion object {
        val ser = SqliteSerializer(
            tables = domain_tables,
            globalSerializers = BasicTS.sqlite + JavaTimeTS.sqlite + KotlinxTimeTS.sqlite + listOf(Serializers.sqlite, Serializers.int)
        )
        val service = SqliteService(config = Properties().apply { this["jdbcUrl"] = "jdbc:sqlite::memory:" }, ser = ser)

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
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
