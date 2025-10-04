package cl.duocuc.myapplication.helpers

import cl.duocuc.myapplication.data.UserDao
import cl.duocuc.myapplication.data.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class CommandDbHelperTest {

    private val userDao: UserDao = mock()
    private val helper: CommandDbHelper = CommandDbHelper(userDao)

    private val testUser = UserEntity(
        id = 0,
        nombre = "Test",
        correo = "test@example.com",
        contrasena = "123456",
        genero = "Otro",
        pais = "Chile"
    )


    @Test
    fun `login correcto y retorna LoginResult con user`() = runBlocking {
        whenever(userDao.login("test@example.com", "123456")).thenReturn(testUser)

        val result = helper.login("test@example.com", "123456")

        assertTrue(result.ok)
        assertEquals("Login exitoso", result.message)
        assertEquals(testUser, result.user)
    }

    @Test
    fun `login falla devuelve LoginResult sin usuario`() = runBlocking {
        whenever(userDao.login("wrong@example.com", "wrong")).thenReturn(null)

        val result = helper.login("wrong@example.com", "wrong")

        assertFalse(result.ok)
        assertEquals("Usuario o contraseña incorrectos", result.message)
        assertNull(result.user)
    }

    @Test
    fun `validateRegister devuelve errores cuando los campos son inválidos`() = runBlocking {
        val invalidUser = testUser.copy(nombre = "", correo = "", contrasena = "123")

        whenever(userDao.findByEmail(any())).thenReturn(null)

        val result = helper.validateRegister(invalidUser)

        assertFalse(result.ok)
        assertTrue(result.errors.contains("El nombre es obligatorio"))
        assertTrue(result.errors.contains("El correo es obligatorio"))
        assertTrue(result.errors.contains("La contraseña debe tener al menos 6 caracteres"))
    }

    @Test
    fun `validateRegister devuelve error si el correo ya existe`() = runBlocking {
        whenever(userDao.findByEmail(testUser.correo)).thenReturn(testUser)

        val result = helper.validateRegister(testUser)

        assertFalse(result.ok)
        assertTrue(result.errors.contains("El correo ya está registrado"))
    }

    @Test
    fun `register inserta el usuario si la validación es correcta`() = runBlocking {
        whenever(userDao.findByEmail(testUser.correo)).thenReturn(null)

        val result = helper.register(testUser)

        assertTrue(result.ok)
        verify(userDao, times(1)).insert(testUser)
    }

    @Test
    fun `register no inserta el usuario si la validación falla`() = runBlocking {
        whenever(userDao.findByEmail(testUser.correo)).thenReturn(testUser)

        val result = helper.register(testUser)

        assertFalse(result.ok)
        verify(userDao, never()).insert(any())
    }

}
