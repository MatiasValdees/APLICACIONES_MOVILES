package cl.duocuc.myapplication.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.duocuc.myapplication.components.MyCard
import cl.duocuc.myapplication.components.MyTopBar
import cl.duocuc.myapplication.models.CheckIn
import cl.duocuc.myapplication.utils.toDDMMYYYYHHmmss
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckInScreen(navController: NavController? = null) {
    var checkIns by remember { mutableStateOf(listOf<CheckIn>()) }
    var patentInput by remember { mutableStateOf("") }
    var selectedCheckIn by remember { mutableStateOf<CheckIn?>(null) }
    val valuePerMinute = 100
    val context = LocalContext.current // Contexto para Toast

    fun addCheckIn(patent: String) {
        if (patent.length < 6) {
            Toast.makeText(context, "La patente debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        if (patent.isNotBlank()) {
            val newCheckIn = CheckIn(
                patent = patent.uppercase(),
                createAt = LocalDateTime.now()
            )
            checkIns = checkIns + newCheckIn
            patentInput = ""
        }
    }

    Scaffold(
        topBar = {
            MyTopBar(
                title = "Llegada de Vehículos",
                navController = navController,
                showLogout = true,
                showBack = false
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = patentInput,
                        onValueChange = { patentInput = it },
                        label = { Text("Patente", fontSize = 18.sp) },
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                    )

                    Button(
                        onClick = { addCheckIn(patentInput) },
                        modifier = Modifier.height(64.dp)
                    ) {
                        Text(
                            "Agregar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(checkIns) { checkIn ->
                        MyCard(
                            title = "Patente: ${checkIn.patent}",
                            subtitle = "Llegada: ${toDDMMYYYYHHmmss(checkIn.createAt)}",
                            labelButton = "Calcular",
                            onClick = { selectedCheckIn = checkIn }
                        )
                    }
                }

                selectedCheckIn?.let { checkIn ->
                    val minutes = java.time.Duration.between(checkIn.createAt, LocalDateTime.now()).toMinutes()
                    val total = minutes * valuePerMinute
                    AlertDialog(
                        onDismissRequest = { selectedCheckIn = null },
                        title = { Text("Detalle de Cobro") },
                        text = {
                            Column {
                                Text("Patente: ${checkIn.patent}", fontSize = 22.sp)
                                Text("Tiempo: $minutes minutos", fontSize = 22.sp)
                                Text("Valor por minuto: $valuePerMinute", fontSize = 22.sp)
                                Text("Total: $total", fontSize = 22.sp)
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                checkIns = checkIns.filter { it != checkIn }
                                selectedCheckIn = null
                            }) {
                                Text("Liberar")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { selectedCheckIn = null }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    )
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckInScreenPreview() {
    CheckInScreen()
}
