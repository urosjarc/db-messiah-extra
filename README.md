<h1 align="center">db-messiah-extra</h1>
<h3 align="center">Extra Utils for Db Messiah, kotlin lib. for enterprise database development</h3>
<br>
<br>
<table width="100%" border="0">
    <tr>
        <td width="50%">
            <h3 align="center"><a href="https://github.com/urosjarc/db-messiah">db-messiah</a></h3>
            <p align="center">Kotlin lib. for enterprise database development</p>
        </td>
        <td width="50%">
            <h3 align="center"><a href="https://github.com/urosjarc/db-analyser">db-analyser</a></h3>
            <p align="center">GUI for db analysis, to help you create complex JOIN statements for SQL or db-messiah.
        </td>
    </tr>
</table>
<br>
<br>

<h2 align="center">Get started</h2>

```kotlin
implementation("com.urosjarc:db-messiah-extra:0.0.1")
```

<br><h3 align="center">DB Serializers</h3>

```kotlin
/** SQLITE */

val sqliteSerializer = SqliteSerializer(
    globalSerializers = BasicTS.sqlite + KotlinxTimeTS.sqlite,
    ...
)

/** POSTGRESQL */

val sqliteSerializer = SqliteSerializer(
    globalSerializers = BasicTS.sqlite + KotlinxTimeTS.sqlite,
    ...
)
```

<br><h3 align="center">JSON Serializers</h3>


```kotlin
/** JSON */

val json = Json {
        serializersModule = SerializersModule {
        contextual(InstantJS)
        contextual(LocalDateJS)
        contextual(LocalTimeJS)
        contextual(UUIDJS)
    }
}

val jsonStr = json.encodeToString(...)
val obj = json.decodeFromString<...>(...)
        
/** KTOR */

install(ContentNegotiation) {
    json(Json {
        serializersModule = SerializersModule {
            contextual(InstantJS)
            contextual(LocalDateJS)
            contextual(LocalTimeJS)
            contextual(UUIDJS)
        }
    })
}
```

<br><h3 align="center">Features</h3>

|   Class   |    COLUMN    |              Databases               |      db-messiah-extra       |    JSON     |
|:---------:|:------------:|:------------------------------------:|:---------------------------:|:-----------:|
|  Instant  |   DATETIME   | Sqlite, Mysql, MSSql, Maria, H2, DB2 | KotlinxInstantTS. DATETIME  |  InstantJS  |
|  Instant  |  TIMESTAMP   |       Derby, Postgres, Oracle        | KotlinxInstantTS. TIMESTAMP |  InstantJS  |
| LocalDate |     DATE     |          :white_check_mark:          |  KotlinxLocalDateTS. DATE   | LocalDateJS |
| LocalTime |     TIME     |    :white_check_mark: but Oracle     |     KotlinxTimeTS.TIME      | LocalDateJS |
| LocalTime | NUMBER(8, 0) |                Oracle                |   KotlinxTimeTS. NUMBER8    | LocalTimeJS |>
|   UUID    |  db-messiah  |              db-messiah              |         db-messiah          |   UUIDJS    |>
