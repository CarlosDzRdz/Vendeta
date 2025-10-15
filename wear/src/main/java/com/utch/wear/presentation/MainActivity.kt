/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.utch.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.utch.wear.presentation.theme.VendetaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

// Enum para representar los posibles estados de la pantalla
enum class GameStatus {
    SUCCESS,
    FAILURE,
    WAITING // Estado inicial mientras no recibe nada
}

@Composable
fun WearApp() {
    // Variable de estado para controlar qué pantalla mostramos.
    // Empezamos en WAITING.
    var status by remember { mutableStateOf(GameStatus.WAITING) }

    VendetaTheme {
        // Usamos un when para decidir qué pantalla mostrar
        when (status) {
            GameStatus.SUCCESS -> ResultScreen(
                backgroundColor = Color(0xFF2C6E49), // Verde oscuro
                icon = Icons.Rounded.Check,
                message = "Correcto"
            )
            GameStatus.FAILURE -> ResultScreen(
                backgroundColor = Color(0xFF881C1C), // Rojo oscuro
                icon = Icons.Rounded.Close,
                message = "Incorrecto"
            )
            GameStatus.WAITING -> {
                // --- Pantalla de espera y botones de prueba ---
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Esperando...")
                    // Botones solo para probar la UI
                    Row {
                        Button(onClick = { status = GameStatus.SUCCESS }) { Text("OK") }
                        Button(onClick = { status = GameStatus.FAILURE }) { Text("Error") }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultScreen(backgroundColor: Color, icon: ImageVector, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = message,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message)
        }
    }
}

// Previews para ver cómo se ve cada estado en Android Studio
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun SuccessPreview() {
    ResultScreen(backgroundColor = Color.Green, icon = Icons.Rounded.Check, message = "Correcto")
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun FailurePreview() {
    ResultScreen(backgroundColor = Color.Red, icon = Icons.Rounded.Close, message = "Incorrecto")
}