package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer
import kotlinx.datetime.*
import java.sql.JDBCType
import java.sql.Timestamp

public object KotlinxInstantTS {

    public val DATETIME: TypeSerializer<Instant> = TypeSerializer(
        kclass = Instant::class,
        dbType = "DATETIME",
        jdbcType = JDBCType.TIMESTAMP,
        decoder = { rs, i, info -> rs.getTimestamp(i).toInstant().toKotlinInstant() },
        encoder = { ps, i, x -> ps.setTimestamp(i, Timestamp.from(x.toJavaInstant())) }
    )
    public val TIMESTAMP: TypeSerializer<Instant> = TypeSerializer(
        kclass = Instant::class,
        dbType = "TIMESTAMP",
        jdbcType = JDBCType.TIMESTAMP,
        decoder = { rs, i, info -> rs.getTimestamp(i).toInstant().toKotlinInstant() },
        encoder = { ps, i, x -> ps.setTimestamp(i, Timestamp.from(x.toJavaInstant())) }
    )
}
