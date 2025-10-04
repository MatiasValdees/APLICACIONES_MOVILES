package cl.duocuc.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cl.duocuc.myapplication.utils.Converters
import java.time.LocalDateTime

@Entity(tableName = "checkins")
@TypeConverters(Converters::class)
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patent: String,
    val createdAt: LocalDateTime
)