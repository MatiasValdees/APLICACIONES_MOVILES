package cl.duocuc.myapplication.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(usuario: UserEntity)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun findByEmail(correo: String): UserEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :email AND contrasena = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

}
