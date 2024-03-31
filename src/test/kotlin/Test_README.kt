import com.urosjarc.dbmessiah.domain.Table
import com.urosjarc.dbmessiah.extra.kotlinx.InstantJS
import com.urosjarc.dbmessiah.extra.kotlinx.LocalDateJS
import com.urosjarc.dbmessiah.extra.kotlinx.LocalTimeJS
import com.urosjarc.dbmessiah.extra.kotlinx.UUIDJS
import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.sqlite.SqliteSerializer
import com.urosjarc.dbmessiah.impl.sqlite.SqliteService
import com.urosjarc.dbmessiah.serializers.BasicTS
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.util.*
import kotlin.test.Test

// START 'Domain'
@Serializable
data class MyTable(
    @Contextual
    val pk: UUID = UUID.randomUUID(),
    val instant: kotlinx.datetime.Instant,
    val localDate: kotlinx.datetime.LocalDate,
    val localTime: kotlinx.datetime.LocalTime,
)

// START 'DB Serializers'
val sqliteSerializer = SqliteSerializer(
    globalSerializers = BasicTS.sqlite + KotlinxTimeTS.sqlite,
    tables = listOf(Table(MyTable::pk)),
)
// STOP

val config = Properties().apply {
    this["jdbcUrl"] = "jdbc:sqlite::memory:"
    this["username"] = "root"
    this["password"] = "root"
}

val sqlite = SqliteService(
    config = config,
    ser = sqliteSerializer
)

fun main() {
    sqlite.autocommit {
        it.table.create<MyTable>()
        val dateTime = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)
        val myTable = MyTable(
            instant = Clock.System.now(),
            localDate = dateTime.date,
            localTime = dateTime.time
        )
        it.row.insert(row = myTable)
        val myTables = it.table.select<MyTable>()
        println(myTables.first().pk == myTable.pk)
    }
    // START 'JSON Serializers'
    /** JSON */

    val json = Json {
        serializersModule = SerializersModule {
            contextual(InstantJS)
            contextual(LocalDateJS)
            contextual(LocalTimeJS)
            contextual(UUIDJS)
        }
    }

    /** KTOR */

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
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
    }

    /** USAGE */

    val dtNow = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)
    val myTable = MyTable(
        instant = Clock.System.now(),
        localDate = dtNow.date,
        localTime = dtNow.time
    )
    val jsonStr = json.encodeToString(myTable)
    val obj = json.decodeFromString<MyTable>(jsonStr)
    assert(obj == myTable)
    // STOP
}

class Test_README {
    @Test
    fun `test README`() {
        main()
    }
}
