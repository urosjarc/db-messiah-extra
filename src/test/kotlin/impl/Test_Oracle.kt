package impl

import org.junit.jupiter.api.BeforeAll
import utils.*
import kotlin.test.Test
import kotlin.test.assertEquals

open class Test_Oracle : Test_Contract {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            oracle.autocommit {
                it.table.drop<Child>(throws = false)
                it.table.drop<Parent>(throws = false)
                it.table.create<Parent>()
                it.table.create<Child>()
            }
        }
    }

    @Test
    override fun `test IdTS UUID`() {
        oracle.autocommit {
            val parent = Parent(col = "col")
            val child = Child(fk = parent.id, col = "col")

            it.row.insert(parent)
            it.row.insert(child)

            assertEquals(expected = parent, actual = it.row.select<Parent>(pk = parent.id))
            assertEquals(expected = child, actual = it.row.select<Child>(pk = child.id))
        }
    }

}