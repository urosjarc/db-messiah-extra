package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.mysql.MysqlSerializer
import com.urosjarc.dbmessiah.impl.mysql.MysqlService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.mysql_schema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Mysql : Test_Contract {

    companion object {
        val ser = MysqlSerializer(
            schemas = listOf(mysql_schema),
            globalSerializers = BasicTS.mysql + JavaTimeTS.mysql + KotlinxTimeTS.mysql + listOf(Serializers.mysql, Serializers.int)
        )
        val service = MysqlService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:mysql://localhost:3307"
                this["username"] = "root"
                this["password"] = "root"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(mysql_schema)
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
