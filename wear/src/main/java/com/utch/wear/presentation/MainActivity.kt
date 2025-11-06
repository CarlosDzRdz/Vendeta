package com.utch.wear.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star // Ícono para la victoria
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.coroutineScope
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.utch.wear.presentation.theme.VendetaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    private val gameStatus = mutableStateOf(GameStatus.WAITING)

    // ⭐ 1. AÑADIMOS EL ESTADO PARA GUARDAR LOS INTENTOS
    private val attemptsLeft = mutableStateOf(3)

    // BroadcastReceiver para recibir mensajes incluso si la app está en segundo plano.
    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra("result") ?: return
            Log.d("VendetaWear", "🔔 Broadcast recibido en MainActivity: $result")
            handleGameResult(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Wearable.getMessageClient(this).addListener(this)

        val filter = IntentFilter("com.utch.wear.GAME_RESULT")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(messageReceiver, filter)
        }
        Log.d("VendetaWear", "✅ MainActivity iniciada y escuchando mensajes")

        setContent {
            // ⭐ 2. PASAMOS LOS INTENTOS A LA UI
            WearApp(status = gameStatus.value, attempts = attemptsLeft.value)
        }
    }

    // Listener principal cuando la Activity está en primer plano.
    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("VendetaWear", "📨 onMessageReceived: path=${messageEvent.path}")

        runOnUiThread {
            when (messageEvent.path) {
                "/game_result" -> {
                    val result = String(messageEvent.data)
                    handleGameResult(result)
                }
                "/game_control" -> {
                    val command = String(messageEvent.data)
                    if (command == "RESTART") handleGameRestart()
                }
                // ⭐ 3. NUEVA RUTA PARA RECIBIR DATOS DEL JUEGO
                "/game_data" -> {
                    val data = String(messageEvent.data)
                    if (data.startsWith("ATTEMPTS:")) {
                        val count = data.substringAfter("ATTEMPTS:").toIntOrNull()
                        if (count != null) {
                            attemptsLeft.value = count
                            Log.d("VendetaWear", "📊 Intentos actualizados a: $count")
                        }
                    }
                }
            }
        }
    }

    // Función centralizada para procesar los resultados.
    // Función centralizada para procesar los resultados.
    private fun handleGameResult(result: String) {
        Log.d("VendetaWear", "🎯 Procesando resultado: $result")
        runOnUiThread {
            when (result) {
                "SUCCESS" -> {
                    gameStatus.value = GameStatus.SUCCESS
                    startReturnToWaitingTimer()
                }
                "FAILURE" -> {
                    gameStatus.value = GameStatus.FAILURE
                    vibrateDevice()
                    // ⭐ ¡AQUÍ ESTÁ EL CAMBIO CLAVE! ⭐
                    // Solo iniciamos el temporizador si aún quedan intentos.
                    // Si attemptsLeft es 1, significa que este era el último intento.
                    if (attemptsLeft.value > 1) {
                        startReturnToWaitingTimer()
                    }
                }
                "WIN" -> {
                    gameStatus.value = GameStatus.WIN
                }
                "GAME_OVER" -> {
                    gameStatus.value = GameStatus.GAME_OVER
                }
            }
        }
    }

    // ⭐ 2. NUEVA FUNCIÓN PARA GESTIONAR EL REINICIO
    private fun handleGameRestart() {
        Log.d("VendetaWear", "🔄 Recibido comando de REINICIO. Volviendo a WAITING.")
        runOnUiThread {
            gameStatus.value = GameStatus.WAITING
        }
    }

    // Inicia un temporizador de 2 segundos para volver a la pantalla de "Esperando...".
    private fun startReturnToWaitingTimer() {
        lifecycle.coroutineScope.launch {
            delay(2000L) // Espera 2 segundos
            gameStatus.value = GameStatus.WAITING
            Log.d("VendetaWear", "⏱️ Temporizador finalizado. Volviendo a WAITING.")
        }
    }

    private fun vibrateDevice() {
        val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(vibrationEffect)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
        unregisterReceiver(messageReceiver)
        Log.d("VendetaWear", "🛑 MainActivity destruida, listeners removidos")
    }
}

// Enum con todos los posibles estados del juego en el reloj.
enum class GameStatus {
    SUCCESS,
    FAILURE,
    WAITING,
    WIN,
    GAME_OVER
}

// ⭐ 6. WearApp AHORA RECIBE EL CONTADOR DE INTENTOS
@Composable
fun WearApp(status: GameStatus, attempts: Int) {
    VendetaTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF212121)),
            contentAlignment = Alignment.Center
        ) {
            when (status) {
                GameStatus.SUCCESS -> ResultScreen(backgroundColor = Color(0xFF2C6E49), icon = Icons.Rounded.Check, message = "Correcto")
                GameStatus.FAILURE -> ResultScreen(backgroundColor = Color(0xFF881C1C), icon = Icons.Rounded.Close, message = "Incorrecto")
                GameStatus.WIN -> ResultScreen(backgroundColor = Color(0xFFF9A825), icon = Icons.Rounded.Star, message = "¡Felicidades!")
                // ⭐ 7. NUEVA PANTALLA DE DERROTA
                GameStatus.GAME_OVER -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "💀", fontSize = 64.sp) // Emoji de calavera
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Moriste...")
                    }
                }
                GameStatus.WAITING -> {
                    // ⭐ 8. MOSTRAMOS EL CONTADOR DE INTENTOS
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Esperando resultado...")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Intentos: $attempts",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
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

// --- Previews para desarrollo ---

// Añade una preview para el nuevo estado (opcional)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun GameOverPreview() {
    WearApp(status = GameStatus.GAME_OVER, attempts = 0)
}

/*
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun WaitingPreview() {
    WearApp(status = GameStatus.WAITING)
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun SuccessPreview() {
    WearApp(status = GameStatus.SUCCESS)
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun FailurePreview() {
    WearApp(status = GameStatus.FAILURE)
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun WinPreview() {
    WearApp(status = GameStatus.WIN)
}
*/