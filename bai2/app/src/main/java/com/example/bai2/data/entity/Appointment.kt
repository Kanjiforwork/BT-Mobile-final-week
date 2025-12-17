package com.example.bai2.data.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Appointment(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val fromTime: String = "",
    val toTime: String = "",
    val personName: String = "",
    val personAvatarUrl: String = "",
    val notificationSent: Boolean = false,
    val createdAt: String = ""
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        fun fromLocalDateTime(dateTime: LocalDateTime): String {
            return dateTime.format(formatter)
        }

        fun toLocalDateTime(dateTimeString: String): LocalDateTime {
            return LocalDateTime.parse(dateTimeString, formatter)
        }
    }

    fun getFromTimeAsLocalDateTime(): LocalDateTime = toLocalDateTime(fromTime)
    fun getToTimeAsLocalDateTime(): LocalDateTime = toLocalDateTime(toTime)
    fun getCreatedAtAsLocalDateTime(): LocalDateTime = toLocalDateTime(createdAt)
}
