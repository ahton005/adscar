import kotlinx.datetime.Instant
import kotlinx.datetime.Instant.Companion.fromEpochMilliseconds
import kotlin.Long.Companion.MIN_VALUE

private val INSTANT_NONE = fromEpochMilliseconds(MIN_VALUE)
val Instant.Companion.NONE get() = INSTANT_NONE
