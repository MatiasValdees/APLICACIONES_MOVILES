package cl.duocuc.myapplication.data

class SignupDbHelper(private val userDao: UserDao) {

    data class ValidationResult(
        val ok: Boolean,
        val errors: List<String> = emptyList()
    )

    data class LoginResult(
        val ok: Boolean,
        val message: String? = null,
        val user: UserEntity? = null
    )

    suspend fun login(email: String, password: String): LoginResult {
        val user = userDao.login(email, password)
        return if (user != null) {
            LoginResult(true, "Login exitoso", user)
        } else {
            LoginResult(false, "Usuario o contraseña incorrectos", null)
        }
    }

    suspend fun validateRegister(user: UserEntity): ValidationResult {
        val errors = mutableListOf<String>()

        if (user.nombre.isBlank()) errors.add("El nombre es obligatorio")
        if (user.correo.isBlank()) errors.add("El correo es obligatorio")
        if (user.contrasena.length < 6) errors.add("La contraseña debe tener al menos 6 caracteres")

        val existingUser = userDao.findByEmail(user.correo)
        if (existingUser != null) errors.add("El correo ya está registrado")

        return ValidationResult(errors.isEmpty(), errors)
    }

    suspend fun register(user: UserEntity): ValidationResult {
        val validation = validateRegister(user)
        if (validation.ok) {
            userDao.insert(user)
        }
        return validation
    }
}
