package impl

import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.maria.MariaSerializer
import com.urosjarc.dbmessiah.impl.maria.MariaService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import org.junit.jupiter.api.BeforeAll
import utils.Child
import utils.Serializers
import utils.Parent
import utils.maria_schema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Maria : Test_Contract {

    companion object {
        val ser = MariaSerializer(
            schemas = listOf(maria_schema),
            globalSerializers = BasicTS.maria + JavaTimeTS.maria + KotlinxTimeTS.maria + listOf(Serializers.maria, Serializers.int)
        )
        val service = MariaService(
            config = Properties().apply {
                this["jdbcUrl"] = "jdbc:mariadb://localhost:3306"
                this["username"] = "root"
                this["password"] = "root"
            },
            ser = ser
        )

        @JvmStatic
        @BeforeAll
        fun init() {
            service.autocommit {
                it.schema.create(maria_schema)
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
