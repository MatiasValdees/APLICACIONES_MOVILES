package cl.duocuc.myapplication.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.myapplication.data.AppDatabase
import cl.duocuc.myapplication.data.SignupDbHelper
import cl.duocuc.myapplication.screens.CheckInScreen
import cl.duocuc.myapplication.screens.ForgotPasswordScreen
import cl.duocuc.myapplication.screens.LoginScreen
import cl.duocuc.myapplication.screens.RegisterScreen
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoutes(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val userDao = remember { db.usuarioDao() }
    val helper = remember { SignupDbHelper(userDao) }
    val checkInDao = db.checkInDao()
    val scope = rememberCoroutineScope()
    Log.d("RoomDB", "DB path: ${context.getDatabasePath("app_db").absolutePath}")
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLogin = { email, password ->
                    scope.launch {
                        val result = helper.login(email, password)
                        if (result.ok && result.user != null) {
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            navController.navigate("check-in/${result.user.nombre}") {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegister = { user ->
                    scope.launch {
                        val result = helper.register(user)
                        if (result.ok) {
                            Toast.makeText(
                                context,
                                "Usuario creado con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                result.errors.joinToString(", "),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                navController = navController
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                userDao = userDao,
                navController = navController
            )
        }

        composable("check-in/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            CheckInScreen(navController = navController, username = username, checkInDao = checkInDao)
        }
    }
}
