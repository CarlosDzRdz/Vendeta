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
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

import com.utch.wear.presentation.theme.VendetaTheme

// ---- MODIFICADO: La Activity ahora escucha mensajes ----
class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    // Variable para cambiar el estado de la UI desde fuera de @Composable
    private val gameStatus = mutableStateOf(GameStatus.WAITING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---- NUEVO: Registrar el oyente de mensajes ----
        Wearable.getMessageClient(this).addListener(this)

        setContent {
            // Pasamos el estado a nuestra UI
            WearApp(status = gameStatus.value)
        }
    }

    // ---- NUEVO: Se ejecuta cuando llega un mensaje del teléfono ----
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/game_result") {
            val result = String(messageEvent.data)
            Log.d("VendetaWatch", "Mensaje recibido: $result")

            // Cambiamos el estado de la UI
            if (result == "SUCCESS") {
                gameStatus.value = GameStatus.SUCCESS
            } else if (result == "FAILURE") {
                gameStatus.value = GameStatus.FAILURE
                // ---- NUEVO: Hacemos vibrar el reloj ----
                vibrateDevice()
            }
        }
    }

    private fun vibrateDevice() {
        val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12 (API 31) y superior
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(vibrationEffect)
        } else {
            // Para versiones anteriores de Android (método obsoleto)
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(vibrationEffect)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ---- NUEVO: Dejamos de escuchar mensajes para no gastar batería ----
        Wearable.getMessageClient(this).removeListener(this)
    }
}

enum class GameStatus {
    SUCCESS,
    FAILURE,
    WAITING
}

@Composable
fun WearApp(status: GameStatus) { // Ahora recibe el estado desde fuera
    VendetaTheme {
        when (status) {
            GameStatus.SUCCESS -> ResultScreen(
                backgroundColor = Color(0xFF2C6E49),
                icon = Icons.Rounded.Check,
                message = "Correcto"
            )
            GameStatus.FAILURE -> ResultScreen(
                backgroundColor = Color(0xFF881C1C),
                icon = Icons.Rounded.Close,
                message = "Incorrecto"
            )
            GameStatus.WAITING -> {
                // --- Pantalla de espera (ya sin botones de prueba) ---
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Esperando resultado...")
                }
            }
        }
    }
}

// ... (El resto del código, ResultScreen y las Previews, se mantiene igual)
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