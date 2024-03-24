package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer
import kotlinx.datetime.*
import java.sql.Date
import java.sql.JDBCType
import java.sql.Time
import java.sql.Timestamp

public object KotlinxLocalTimeTS {

    public val TIME: TypeSerializer<LocalTime> = TypeSerializer(
        kclass = LocalTime::class,
        dbType = "TIME",
        jdbcType = JDBCType.TIME,
        decoder = { rs, i, info -> rs.getTime(i).toLocalTime().toKotlinLocalTime() },
        encoder = { ps, i, x -> ps.setTime(i, Time.valueOf(x.toJavaLocalTime())) }
    )
    public val NUMBER8: TypeSerializer<LocalTime> = TypeSerializer(
        kclass = LocalTime::class,
        dbType = "NUMBER(8, 0)",
        jdbcType = JDBCType.NUMERIC,
        decoder = { rs, i, info -> LocalTime.fromMillisecondOfDay(rs.getInt(i)) },
        encoder = { ps, i, x -> ps.setInt(i, x.toMillisecondOfDay()) }
    )

}
