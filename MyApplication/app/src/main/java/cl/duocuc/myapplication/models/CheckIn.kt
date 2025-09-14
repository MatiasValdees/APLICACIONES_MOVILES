package cl.duocuc.myapplication.models

import java.time.LocalDateTime

data class CheckIn (
    val patent: String,
    val createAt: LocalDateTime
)