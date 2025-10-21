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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.utch.wear.presentation.theme.VendetaTheme

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    private val gameStatus = mutableStateOf(GameStatus.WAITING)

    // ⭐ NUEVO: BroadcastReceiver para recibir mensajes del servicio
    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra("result") ?: return
            Log.d("VendetaWear", "🔔 Broadcast recibido en MainActivity: $result")
            handleGameResult(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registrar listener de mensajes (método principal)
        Wearable.getMessageClient(this).addListener(this)

        // ⭐ NUEVO: Registrar broadcast receiver (método secundario)
        val filter = IntentFilter("com.utch.wear.GAME_RESULT")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(messageReceiver, filter)
        }

        Log.d("VendetaWear", "✅ MainActivity iniciada y escuchando mensajes")

        setContent {
            WearApp(status = gameStatus.value)
        }
    }

    // ⭐ MEJORADO: Método que recibe directamente (cuando la app está abierta)
    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("VendetaWear", "📨 onMessageReceived llamado directamente: ${messageEvent.path}")

        if (messageEvent.path == "/game_result") {
            val result = String(messageEvent.data)
            handleGameResult(result)
        }
    }

    // ⭐ NUEVO: Función centralizada para manejar resultados
    private fun handleGameResult(result: String) {
        Log.d("VendetaWear", "🎯 Procesando resultado: $result")

        runOnUiThread {
            when (result) {
                "SUCCESS" -> {
                    gameStatus.value = GameStatus.SUCCESS
                    Log.d("VendetaWear", "✅ Estado cambiado a SUCCESS")
                }
                "FAILURE" -> {
                    gameStatus.value = GameStatus.FAILURE
                    vibrateDevice()
                    Log.d("VendetaWear", "❌ Estado cambiado a FAILURE con vibración")
                }
            }
        }
    }

    private fun vibrateDevice() {
        val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(vibrationEffect)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reiniciar el estado al volver a la app
        gameStatus.value = GameStatus.WAITING
        Log.d("VendetaWear", "🔄 Estado reiniciado a WAITING")
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
        unregisterReceiver(messageReceiver)
        Log.d("VendetaWear", "🛑 MainActivity destruida, listeners removidos")
    }
}

enum class GameStatus {
    SUCCESS,
    FAILURE,
    WAITING
}

@Composable
fun WearApp(status: GameStatus) {
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