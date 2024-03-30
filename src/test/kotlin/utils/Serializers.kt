package utils

import com.urosjarc.dbmessiah.data.TypeSerializer
import com.urosjarc.dbmessiah.extra.kotlinx.InstantJS
import com.urosjarc.dbmessiah.extra.kotlinx.LocalDateJS
import com.urosjarc.dbmessiah.extra.kotlinx.LocalTimeJS
import com.urosjarc.dbmessiah.extra.kotlinx.UUIDJS
import com.urosjarc.dbmessiah.serializers.IdTS
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val json = Json {
    serializersModule = SerializersModule {
        contextual(InstantJS)
        contextual(LocalDateJS)
        contextual(LocalTimeJS)
        contextual(UUIDJS)
    }
}

internal object Serializers {
    val int = IdTS.int({ Id<Any>(it) }, { it.value })
    val sqlite: TypeSerializer<UId<Any>> = IdTS.uuid.sqlite { UId(it) }
    val postgresql: TypeSerializer<UId<Any>> = IdTS.uuid.postgresql({ UId(it) }, { it.value })
    val oracle: TypeSerializer<UId<Any>> = IdTS.uuid.oracle { UId(it) }
    val mysql: TypeSerializer<UId<Any>> = IdTS.uuid.mysql { UId(it) }
    val maria: TypeSerializer<UId<Any>> = IdTS.uuid.maria({ UId(it) }, { it.value })
    val mssql: TypeSerializer<UId<Any>> = IdTS.uuid.mssql { UId(it) }
    val h2: TypeSerializer<UId<Any>> = IdTS.uuid.h2({ UId(it) }, { it.value })
    val derby: TypeSerializer<UId<Any>> = IdTS.uuid.derby { UId(it) }
    val db2: TypeSerializer<UId<Any>> = IdTS.uuid.db2 { UId(it) }
}
