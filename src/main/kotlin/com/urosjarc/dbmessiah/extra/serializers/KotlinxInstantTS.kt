package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.sql.JDBCType
import java.sql.Timestamp

/**
 * Providing serializers for [Instant] class.
 */
public object KotlinxInstantTS {

    /**
     * Serializer [Instant] class to DATETIME database type.
     */
    public val DATETIME: TypeSerializer<Instant> = TypeSerializer(
        kclass = Instant::class,
        dbType = "DATETIME",
        jdbcType = JDBCType.TIMESTAMP,
        decoder = { rs, i, info -> rs.getTimestamp(i).toInstant().toKotlinInstant() },
        encoder = { ps, i, x -> ps.setTimestamp(i, Timestamp.from(x.toJavaInstant())) }
    )

    /**
     * Serializer [Instant] class to TIMESTAMP database type.
     */
    public val TIMESTAMP: TypeSerializer<Instant> = TypeSerializer(
        kclass = Instant::class,
        dbType = "TIMESTAMP",
        jdbcType = JDBCType.TIMESTAMP,
        decoder = { rs, i, info -> rs.getTimestamp(i).toInstant().toKotlinInstant() },
        encoder = { ps, i, x -> ps.setTimestamp(i, Timestamp.from(x.toJavaInstant())) }
    )
}
