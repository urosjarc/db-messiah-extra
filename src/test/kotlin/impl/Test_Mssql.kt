package impl

import org.junit.jupiter.api.BeforeAll
import utils.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Mssql : Test_Contract {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            mssql.autocommit {
                it.schema.create(mssql_schema)
                it.table.drop<Child>()
                it.table.drop<Parent>()
                it.table.create<Parent>()
                it.table.create<Child>()
            }
        }
    }

    @Test
    override fun `test IdTS UUID`() {
        mssql.autocommit {
            val parent = Parent(col = "col")
            val child = Child(fk = parent.id, col = "col")

            it.row.insert(parent)
            it.row.insert(child)

            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }

}
