package cl.duocuc.myapplication.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import cl.duocuc.myapplication.components.MyTopBar
import cl.duocuc.myapplication.models.User

class RegisterActivity : ComponentActivity() {

    companion object {
        val users = mutableStateListOf(
            User(
                nombre = "matias",
                correo = "matias",
                contrasena = "1234",
                genero = "Masculino",
                pais = "Chile"
            ),
            User(
                nombre = "miguel",
                correo = "miguel@duocuc.cl",
                contrasena = "duoc",
                genero = "Masculino",
                pais = "Chile"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RegisterScreen(
                    onRegister = { nombre, correo, contrasena, genero, pais ->
                        if (users.size < 5) {
                            users.add(User(nombre, correo, contrasena, genero, pais))
                            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Máximo 5 usuarios", Toast.LENGTH_SHORT).show()
                        }
                    },
                    navController = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String, String) -> Unit,
    navController: NavController? = null
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    val opcionesGenero = listOf("Masculino", "Femenino", "Otro")
    var generoSeleccionado by remember { mutableStateOf(opcionesGenero[0]) }

    val paises = listOf("Argentina", "Brasil", "Chile", "Uruguay", "Paraguay")
    var paisSeleccionado by remember { mutableStateOf("Chile") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopBar(
                title = "Registro",
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
                Text("Registro de Usuario", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre",fontSize = 26.sp) },
                    textStyle = TextStyle(fontSize = 26.sp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico",fontSize = 26.sp) },
                    textStyle = TextStyle(fontSize = 26.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña",fontSize = 26.sp) },
                    textStyle = TextStyle(fontSize = 20.sp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = aceptaTerminos, onCheckedChange = { aceptaTerminos = it },)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Acepto los términos y condiciones", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(22.dp))
                Text("Género", fontSize = 24.sp, fontWeight = FontWeight.Medium)
                opcionesGenero.forEach { opcion ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (opcion == generoSeleccionado),
                                onClick = { generoSeleccionado = opcion },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (opcion == generoSeleccionado),
                            onClick = { generoSeleccionado = opcion }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = opcion, fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = paisSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("País", fontSize = 24.sp) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        paises.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion, fontSize = 26.sp) },
                                onClick = {
                                    paisSeleccionado = opcion
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        onRegister(nombre, correo, contrasena, generoSeleccionado, paisSeleccionado)
                    },
                    enabled = nombre.isNotEmpty() &&
                            correo.isNotEmpty() &&
                            contrasena.isNotEmpty() &&
                            aceptaTerminos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text("Registrar", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegister = { _, _, _, _, _ -> }, navController = null)
}
