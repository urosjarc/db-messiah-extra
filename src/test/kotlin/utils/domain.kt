package utils

import com.urosjarc.dbmessiah.extra.kotlinx.UUIDSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
@JvmInline
value class Id<T>(
    @Serializable(with = UUIDSerializer::class)
    val value: UUID = UUID.randomUUID()
) {
    override fun toString(): String {
        return this.value.toString()
    }
}

@Serializable
data class Parent(
    val id: Id<Parent> = Id(),
    val col: String
)

data class AutoParent(
    var id: Id<Child>? = null,
    val col: String
)

data class Child(
    val id: Id<Child> = Id(),
    val fk: Id<Parent>,
    val col: String,
    val kx_date: kotlinx.datetime.LocalDate = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC).date,
    val kx_dateTime: kotlinx.datetime.LocalDateTime = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC),
    val kx_instant: kotlinx.datetime.Instant = Clock.System.now(),
    val j_date: java.time.LocalDate = java.time.LocalDate.now(),
    val j_dateTime: java.time.LocalDateTime = java.time.LocalDateTime.now(),
    val j_instant: java.time.Instant = java.time.Instant.now(),
)
