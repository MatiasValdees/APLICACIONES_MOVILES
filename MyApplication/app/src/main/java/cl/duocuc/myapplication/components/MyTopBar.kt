package cl.duocuc.myapplication.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String,
    navController: NavController? = null,
    showBack: Boolean = true,
    showLogout: Boolean = false,
    onLogout: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                Button(onClick = {
                    showLogoutDialog = false
                    onLogout()
                    navController?.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    TopAppBar(
        title = { Text(title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (showBack && navController != null) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        },
        actions = {
            if (showLogout) {
                IconButton(onClick = { showLogoutDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión"
                    )
                }
            }
            actions()
        },
        modifier = Modifier.height(56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
