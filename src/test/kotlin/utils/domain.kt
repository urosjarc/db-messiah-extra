package utils

import com.urosjarc.dbmessiah.extra.kotlinx.UUIDSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.math.absoluteValue

@Serializable
@JvmInline
value class Id<T>(val value: Int) {
    override fun toString(): String {
        return this.value.toString()
    }
}

@Serializable
@JvmInline
value class UId<T>(
    @Serializable(with = UUIDSerializer::class)
    val value: UUID = UUID.randomUUID()
) {
    override fun toString(): String {
        return this.value.toString()
    }
}

@Serializable
data class Parent(
    val id: UId<Parent> = UId(),
    val col: String
)

data class AutoParent(
    var id: UId<AutoParent>? = null,
    val col: String
)
data class AutoChild(
    var id: Id<AutoChild>? = null,
    val col: String
)

class Child(
    val id: UId<Child> = UId(),
    val fk: UId<Parent>,
    val col: String,
    val kx_instant: kotlinx.datetime.Instant = Clock.System.now(),
    val kx_date: kotlinx.datetime.LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    val kx_time: kotlinx.datetime.LocalTime = Clock.System.now().toLocalDateTime(TimeZone.UTC).time,
    val j_date: java.time.LocalDate = java.time.LocalDate.now(),
    val j_time: java.time.LocalTime = java.time.LocalTime.now(),
    val j_instant: java.time.Instant = java.time.Instant.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Child

        if (id != other.id) return false
        if (fk != other.fk) return false
        if (col != other.col) return false
        if (kx_date != other.kx_date) return false
        if ((kx_time.toSecondOfDay() - other.kx_time.toSecondOfDay()).absoluteValue > 1L) return false
        if ((kx_instant.epochSeconds - other.kx_instant.epochSeconds).absoluteValue > 1L) return false

        if (j_date != other.j_date) return false
        if ((j_time.toSecondOfDay() - other.j_time.toSecondOfDay()).absoluteValue > 1L) return false
        if ((j_instant.epochSecond - other.j_instant.epochSecond).absoluteValue > 1L) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fk.hashCode()
        result = 31 * result + col.hashCode()
        result = 31 * result + kx_instant.hashCode()
        result = 31 * result + kx_date.hashCode()
        result = 31 * result + kx_time.hashCode()
        result = 31 * result + j_date.hashCode()
        result = 31 * result + j_time.hashCode()
        result = 31 * result + j_instant.hashCode()
        return result
    }

}
