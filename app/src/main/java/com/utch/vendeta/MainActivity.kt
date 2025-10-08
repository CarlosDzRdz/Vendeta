package com.utch.vendeta // Tu package name es correcto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.utch.vendeta.ui.theme.VendetaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VendetaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VendetaScreen()
                }
            }
        }
    }
}

@Composable
fun VendetaScreen() {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>("Aún no has escaneado nada") }

    // Obtenemos una instancia del scanner.
    val scanner: GmsBarcodeScanner = GmsBarcodeScanning.getClient(context)

    // Este es el "lanzador" que inicia el scanner y procesa el resultado.
    // Usar GmsBarcodeScanning.createActivityResultContract() es la forma moderna y correcta.
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = GmsBarcodeScanning.createActivityResultContract()
    ) { result: Barcode? ->
        scannedValue = result?.rawValue ?: "El escaneo fue cancelado."
    }

    // Este es el "lanzador" que pide el permiso de la cámara.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, ahora sí lanzamos el scanner
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    scannedValue = barcode.rawValue
                }
                .addOnFailureListener { e ->
                    scannedValue = "Error al escanear: ${e.message}"
                }
        } else {
            // Permiso denegado, informamos al usuario
            Toast.makeText(context, "El permiso de la cámara es necesario.", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Vendeta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                // Comprobamos si ya tenemos el permiso
                val hasCameraPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasCameraPermission) {
                    // Si ya lo tenemos, lanzamos el scanner directamente
                    scanner.startScan()
                        .addOnSuccessListener { barcode ->
                            scannedValue = barcode.rawValue
                        }
                        .addOnFailureListener { e ->
                            scannedValue = "Error al escanear: ${e.message}"
                        }
                } else {
                    // Si no lo tenemos, lo pedimos
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Escanear QR")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Resultado: ${scannedValue}",
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VendetaScreenPreview() {
    VendetaTheme {
        VendetaScreen()
    }
}