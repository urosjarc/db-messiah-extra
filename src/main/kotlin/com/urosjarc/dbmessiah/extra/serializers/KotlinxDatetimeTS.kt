package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer
import kotlinx.datetime.*
import java.sql.JDBCType
import java.sql.Timestamp

public class KotlinxDatetimeTS {
    public class localDate {

        internal val TIMESTAMP = TypeSerializer(
            kclass = LocalDate::class,
            dbType = "TIMESTAMP",
            jdbcType = JDBCType.TIMESTAMP,
            decoder = { rs, i, info -> rs.getTimestamp(i).toInstant().toKotlinInstant().toLocalDateTime(TimeZone.UTC).date },
            encoder = { ps, i, x -> ps.setTimestamp(i, Timestamp.from(x.atStartOfDayIn(timeZone = TimeZone.UTC).toJavaInstant())) }
        )

    }
}
