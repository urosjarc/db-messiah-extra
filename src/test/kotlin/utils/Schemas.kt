package utils

import com.urosjarc.dbmessiah.domain.C
import com.urosjarc.dbmessiah.domain.Table
import com.urosjarc.dbmessiah.impl.db2.Db2Schema
import com.urosjarc.dbmessiah.impl.derby.DerbySchema
import com.urosjarc.dbmessiah.impl.h2.H2Schema
import com.urosjarc.dbmessiah.impl.maria.MariaSchema
import com.urosjarc.dbmessiah.impl.mssql.MssqlSchema
import com.urosjarc.dbmessiah.impl.mysql.MysqlSchema
import com.urosjarc.dbmessiah.impl.oracle.OracleSchema
import com.urosjarc.dbmessiah.impl.postgresql.PgSchema
import kotlin.reflect.KProperty1

fun <T : Any> createTable(primaryKey: KProperty1<T, *>): Table<T> {
    val foreignKeys = Table.getInlineTypedForeignKeys(primaryKey = primaryKey)
    return Table(
        primaryKey = primaryKey,
        foreignKeys = foreignKeys,
        constraints = foreignKeys.map { it.first to listOf(C.CASCADE_DELETE) })
}

val domain_tables = listOf(
    createTable(Parent::id),
    createTable(Child::id),
)
val autouuid_domain_tables = listOf(
    createTable(Parent::id),
    createTable(Child::id),
    createTable(AutoParent::id),
)

val h2_schema = H2Schema(name = "domain", tables = autouuid_domain_tables)
val db2_schema = Db2Schema(name = "domain", tables = domain_tables)
val derby_schema = DerbySchema(name = "domain", tables = domain_tables)
val maria_schema = MariaSchema(name = "domain", tables = domain_tables)
val mssql_schema = MssqlSchema(name = "domain", tables = domain_tables)
val mysql_schema = MysqlSchema(name = "domain", tables = domain_tables)
val oracle_schema = OracleSchema(name = "SYSTEM", tables = domain_tables)
val pg_schema = PgSchema(name = "domain", tables = autouuid_domain_tables)
