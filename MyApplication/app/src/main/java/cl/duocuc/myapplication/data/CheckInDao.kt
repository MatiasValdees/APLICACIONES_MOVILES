package cl.duocuc.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckInDao {
    @Insert
    suspend fun insert(checkIn: CheckInEntity)

    @Query("SELECT * FROM checkins ORDER BY createdAt DESC")
    suspend fun getAll(): List<CheckInEntity>

    @Query("DELETE FROM checkins WHERE id = :id")
    suspend fun deleteById(id: Int)
}