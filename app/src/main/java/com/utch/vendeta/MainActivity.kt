package com.utch.vendeta

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.wearable.Wearable
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.utch.vendeta.ui.theme.VendetaTheme

// (Las clases Riddle y la lista gameRiddles se mantienen igual)
data class Riddle(
    val clue: String,
    val correctAnswer: String
)

val gameRiddles = listOf(
    Riddle(clue = "Me abres todos los días pero no soy una puerta. Tengo hojas pero no soy un árbol. ¿Dónde estoy?", correctAnswer = "BIBLIOTECA_NIVEL_1"),
    Riddle(clue = "El lugar donde el conocimiento se sirve caliente y el sueño se combate a sorbos.", correctAnswer = "CAFETERIA_NIVEL_2"),
    Riddle(clue = "Aquí es donde las ideas cobran vida en pantallas y teclados. Es el corazón digital de la escuela.", correctAnswer = "LABORATORIO_NIVEL_3"),
    Riddle(clue = "Donde las voces se elevan sin gritar y las emociones se representan sin palabras. ¿Dónde estoy?", correctAnswer = "AUDITORIO_NIVEL_4"),
    Riddle(clue = "Aquí se cultiva el cuerpo con esfuerzo y disciplina. El sudor es parte del aprendizaje.", correctAnswer = "CANCHAS_NIVEL_5")
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VendetaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    VendetaScreen()
                }
            }
        }
    }
}

@Composable
fun VendetaScreen() {
    val context = LocalContext.current
    var currentRiddleIndex by remember { mutableStateOf(0) }
    var gameFinished by remember { mutableStateOf(false) }
    val currentRiddle = gameRiddles[currentRiddleIndex]

    // ---- NUEVO: Cliente para comunicación con el reloj ----
    val messageClient = Wearable.getMessageClient(context)

    // --- NUEVA FUNCIÓN: Para enviar mensajes al reloj ---
    fun sendMessageToWearable(result: String) {
        // Busca los dispositivos conectados (nodos)
        Wearable.getNodeClient(context).connectedNodes.addOnSuccessListener { nodes ->
            nodes.forEach { node ->
                // Envía el mensaje a cada nodo encontrado
                messageClient.sendMessage(node.id, "/game_result", result.toByteArray())
                    .addOnSuccessListener { Log.d("Vendeta", "Mensaje '$result' enviado a ${node.displayName}") }
                    .addOnFailureListener { e -> Log.e("Vendeta", "Error al enviar mensaje", e) }
            }
        }
    }

    val scannerOptions = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE)
        .build()
    val scanner = GmsBarcodeScanning.getClient(context, scannerOptions)

    val handleScanResult = handleScanResult@{ scannedText: String? ->
        if (scannedText == null) {
            Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            return@handleScanResult
        }

        if (scannedText == currentRiddle.correctAnswer) {
            // ---- MODIFICADO: Envía "SUCCESS" al reloj ----
            sendMessageToWearable("SUCCESS")
            if (currentRiddleIndex < gameRiddles.size - 1) {
                currentRiddleIndex++
                Toast.makeText(context, "¡Correcto! Siguiente acertijo.", Toast.LENGTH_SHORT).show()
            } else {
                gameFinished = true
            }
        } else {
            // ---- MODIFICADO: Envía "FAILURE" al reloj ----
            sendMessageToWearable("FAILURE")
            Toast.makeText(context, "Incorrecto. Intenta de nuevo.", Toast.LENGTH_LONG).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scanner.startScan()
                .addOnSuccessListener { barcode -> handleScanResult(barcode.rawValue) }
                .addOnCanceledListener { handleScanResult(null) }
                .addOnFailureListener { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
        } else {
            Toast.makeText(context, "El permiso de la cámara es necesario.", Toast.LENGTH_LONG).show()
        }
    }

    // --- El resto de la UI (Column, Text, Button) se mantiene igual ---
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Vendeta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        if (gameFinished) {
            Text(
                text = "¡Felicidades, has escapado!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = currentRiddle.clue,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }

        if (!gameFinished) {
            Button(onClick = {
                val hasCameraPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasCameraPermission) {
                    scanner.startScan()
                        .addOnSuccessListener { barcode -> handleScanResult(barcode.rawValue) }
                        .addOnCanceledListener { handleScanResult(null) }
                        .addOnFailureListener { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Escanear Pista")
            }
        }
    }
}