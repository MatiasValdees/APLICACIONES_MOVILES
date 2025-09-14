package cl.duocuc.myapplication.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.myapplication.models.User
import cl.duocuc.myapplication.screens.CheckInScreen
import cl.duocuc.myapplication.screens.ForgotPasswordScreen
import cl.duocuc.myapplication.screens.LoginScreen
import cl.duocuc.myapplication.screens.RegisterActivity
import cl.duocuc.myapplication.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val CHECK_IN = "check-in"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoutes(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLogin = { email, password ->
                    val usuarioValido = RegisterActivity.users.any { user ->
                        user.correo == email && user.contrasena == password
                    }
                    if (usuarioValido) {
                        Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.CHECK_IN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegister = { nombre, correo, contrasena, genero, pais ->
                    val existe = RegisterActivity.users.any { it.correo == correo }
                    if (existe) {
                        Toast.makeText(context, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                    } else {
                        RegisterActivity.users.add(
                            User(nombre, correo, contrasena, genero, pais)
                        )
                        Toast.makeText(context, "Usuario creado con éxito", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    }
                },
                navController = navController
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onSendReset = { email ->
                    val existe = RegisterActivity.users.any { it.correo == email }
                    if (existe) {
                        Toast.makeText(
                            context,
                            "Se envió un correo de recuperación",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Correo no registrado", Toast.LENGTH_SHORT).show()
                    }
                },
                navController = navController
            )
        }

        composable(Routes.CHECK_IN) {
            CheckInScreen(navController = navController)
        }
    }
}
