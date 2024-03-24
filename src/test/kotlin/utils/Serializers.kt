package utils

import com.urosjarc.dbmessiah.data.TypeSerializer
import com.urosjarc.dbmessiah.impl.db2.Db2Serializer
import com.urosjarc.dbmessiah.impl.derby.DerbySerializer
import com.urosjarc.dbmessiah.impl.h2.H2Serializer
import com.urosjarc.dbmessiah.impl.maria.MariaSerializer
import com.urosjarc.dbmessiah.impl.mssql.MssqlSerializer
import com.urosjarc.dbmessiah.impl.mysql.MysqlSerializer
import com.urosjarc.dbmessiah.impl.oracle.OracleSerializer
import com.urosjarc.dbmessiah.impl.postgresql.PgSerializer
import com.urosjarc.dbmessiah.impl.sqlite.SqliteSerializer
import com.urosjarc.dbmessiah.serializers.AllTS
import com.urosjarc.dbmessiah.serializers.IdTS

private object IDTS {
    val sqlite: TypeSerializer<Id<Any>> = IdTS.uuid.sqlite { Id(it) }
    val postgresql: TypeSerializer<Id<Any>> = IdTS.uuid.postgresql({ Id(it) }, { it.value })
    val oracle: TypeSerializer<Id<Any>> = IdTS.uuid.oracle { Id(it) }
    val mysql: TypeSerializer<Id<Any>> = IdTS.uuid.mysql { Id(it) }
    val maria: TypeSerializer<Id<Any>> = IdTS.uuid.maria({ Id(it) }, { it.value })
    val mssql: TypeSerializer<Id<Any>> = IdTS.uuid.mssql { Id(it) }
    val h2: TypeSerializer<Id<Any>> = IdTS.uuid.h2({ Id(it) }, { it.value })
    val derby: TypeSerializer<Id<Any>> = IdTS.uuid.derby { Id(it) }
    val db2: TypeSerializer<Id<Any>> = IdTS.uuid.db2 { Id(it) }
}

val db2_serializer = Db2Serializer(
    schemas = listOf(db2_schema),
    globalSerializers = AllTS.db2 + listOf(IDTS.db2)
)
val derby_serializer = DerbySerializer(
    schemas = listOf(derby_schema),
    globalSerializers = AllTS.derby + listOf(IDTS.derby)
)
val h2_serializer = H2Serializer(
    schemas = listOf(h2_schema),
    globalSerializers = AllTS.h2 + listOf(IDTS.h2)
)
val maria_serializer = MariaSerializer(
    schemas = listOf(maria_schema),
    globalSerializers = AllTS.maria + listOf(IDTS.maria)
)
val mssql_serializer = MssqlSerializer(
    schemas = listOf(mssql_schema),
    globalSerializers = AllTS.mssql + listOf(IDTS.mssql)
)
val mysql_serializer = MysqlSerializer(
    schemas = listOf(mysql_schema),
    globalSerializers = AllTS.mysql + listOf(IDTS.mysql)
)
val oracle_serializer = OracleSerializer(
    schemas = listOf(oracle_schema),
    globalSerializers = AllTS.oracle + listOf(IDTS.oracle)
)

val postgresql_serializer = PgSerializer(
    schemas = listOf(pg_schema),
    globalSerializers = AllTS.postgresql + listOf(IDTS.postgresql)
)
val sqlite_serializer = SqliteSerializer(
    tables = domain_tables,
    globalSerializers = AllTS.sqlite + listOf(IDTS.sqlite)
)
