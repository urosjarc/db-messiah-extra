package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.sql.Date
import java.sql.JDBCType

/**
 * Providing serializers for [LocalDate] class.
 */
public object KotlinxLocalDateTS {

    /**
     * Serializer [LocalDate] class to DATE database type.
     */
    public val DATE: TypeSerializer<LocalDate> = TypeSerializer(
        kclass = LocalDate::class,
        dbType = "DATE",
        jdbcType = JDBCType.DATE,
        decoder = { rs, i, _ -> rs.getDate(i).toLocalDate().toKotlinLocalDate() },
        encoder = { ps, i, x -> ps.setDate(i, Date.valueOf(x.toJavaLocalDate())) }
    )

}
