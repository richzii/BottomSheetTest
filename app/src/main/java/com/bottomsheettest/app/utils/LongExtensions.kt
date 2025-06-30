package com.bottomsheettest.app.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.asReadableDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(this))
}