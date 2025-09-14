package cl.duocuc.myapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
private val dateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

@RequiresApi(Build.VERSION_CODES.O)
fun toDDMMYYYYHHmmss(dateTime: LocalDateTime): String {
    return dateTime.format(dateTimeFormatter)
}