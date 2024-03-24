package com.urosjarc.dbmessiah.extra.serializers

import com.urosjarc.dbmessiah.data.TypeSerializer

public object KotlinxTimeTS {
    private val DATETIME = listOf(KotlinxInstantTS.DATETIME, KotlinxLocalDateTS.DATE, KotlinxLocalTimeTS.TIME)
    private val TIMESTAMP = listOf(KotlinxInstantTS.TIMESTAMP, KotlinxLocalDateTS.DATE, KotlinxLocalTimeTS.TIME)

    public val sqlite: List<TypeSerializer<out Any>> = DATETIME
    public val postgresql: List<TypeSerializer<out Any>> = TIMESTAMP
    public val oracle: List<TypeSerializer<out Any>> = listOf(KotlinxInstantTS.TIMESTAMP, KotlinxLocalDateTS.DATE, KotlinxLocalTimeTS.NUMBER8)
    public val mysql: List<TypeSerializer<out Any>> = DATETIME
    public val mssql: List<TypeSerializer<out Any>> = DATETIME
    public val maria: List<TypeSerializer<out Any>> = DATETIME
    public val h2: List<TypeSerializer<out Any>> = DATETIME
    public val derby: List<TypeSerializer<out Any>> = TIMESTAMP
    public val db2: List<TypeSerializer<out Any>> = DATETIME


}
