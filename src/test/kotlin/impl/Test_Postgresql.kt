package impl

import org.junit.jupiter.api.BeforeAll
import utils.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class Test_Postgresql : Test_Contract {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            pg.autocommit {
                it.schema.create(pg_schema)
                it.table.drop<Child>()
                it.table.drop<Parent>()
                it.table.drop<AutoParent>(throws = false)
                it.table.create<Parent>()
                it.table.create<AutoParent>()
                it.table.create<Child>()
            }
        }
    }


    @Test
    override fun `test IdTS UUID`() {
        pg.autocommit {
            val parent = Parent(col = "col")
            val autoParent = AutoParent(id = null, col = "col")
            val child = Child(fk = parent.id, col = "col")

            it.row.insert(autoParent)
            it.row.insert(parent)
            it.row.insert(child)

            assertNotNull(actual = autoParent.id)
            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }
}
