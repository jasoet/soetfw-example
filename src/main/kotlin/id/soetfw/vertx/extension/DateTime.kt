package id.soetfw.vertx.extension

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */
private val log = logger("DateTimeExtension")

fun Int.toYear(): Year {
    return Year.of(this)
}

fun LocalDateTime?.isoDateFormat(): String? {
    return this?.format(DateTimeFormatter.ISO_DATE)
}

fun LocalDateTime?.isoTimeFormat(): String? {
    return this?.format(DateTimeFormatter.ISO_TIME)
}

fun LocalDateTime?.isoDateTimeFormat(): String? {
    return this?.format(DateTimeFormatter.ISO_DATE_TIME)
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun LocalDateTime.toMilliSeconds(): Long {
    return this.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@Throws(DateTimeParseException::class)
fun String.toLocalDate(pattern: String): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
}

fun Long.fromUnixTimestamp(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this * 1000), ZoneId.systemDefault())
}

fun String.fromUnixTimestamp(): LocalDateTime {
    return this.toLong().fromUnixTimestamp()
}

fun LocalDate?.toFormat(pattern: String): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern(pattern)) ?: ""
    } catch (e: Exception) {
        log.error("Exception when format $this to $pattern", e)
        return ""
    }
}

fun String.toIDFormat(isoFormat: Boolean = true, withTime: Boolean = true): String {

    val datetime = if (isoFormat) this.split("T") else this.split(" ")

    return try {

        val date = datetime[0].split("-")

        val months = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
        val monthNumber = date[1].toInt()
        val monthName = months[monthNumber - 1]

        val formattedDate = "${date[2]} $monthName ${date[0]}"
        val formattedTime = if (isoFormat) {
            val time = datetime[1].split(":")
            val second = time.last().split(".")
            "${time[0]}:${time[1]}:${second[0]}"
        } else {
            datetime[1]
        }

        if (withTime) "$formattedDate $formattedTime" else formattedDate

    } catch (e: Exception) {
        log.error("Exception when format $this to ID format", e)
        return ""
    }

}
