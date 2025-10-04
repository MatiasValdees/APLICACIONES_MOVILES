package cl.duocuc.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.myapplication.components.MyTopBar
import cl.duocuc.myapplication.data.UserDao
import cl.duocuc.myapplication.data.UserEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    userDao: UserDao,
    navController: NavController? = null
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MyTopBar(
                title = "Recuperación de Contraseña",
                navController = navController
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(40.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", fontSize = 26.sp) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 26.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            scope.launch {
                                val user = userDao.findByEmail(email.trim())
                                if (user != null) {
                                    Toast.makeText(
                                        context,
                                        "Se envió un enlace a ${user.correo}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "El correo no está registrado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Ingrese un correo", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text("Enviar enlace", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    // Para preview se puede pasar un DAO falso vacío
    ForgotPasswordScreen(
        userDao = object : UserDao {
            override suspend fun insert(usuario: UserEntity) {}
            override suspend fun findByEmail(correo: String) = null
            override suspend fun login(email: String, password: String) = null
        },
        navController = null
    )
}
