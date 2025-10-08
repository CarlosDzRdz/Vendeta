package com.utch.vendeta // Corregido para coincidir con la ruta del archivo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    // ---- NUEVO ----
    // Contexto de la aplicación, necesario para el scanner y los permisos
    val context = LocalContext.current
    // Variable para guardar y mostrar el valor del QR escaneado
    var scannedValue by remember { mutableStateOf<String?>("Aún no has escaneado nada") }

    // Obtenemos una instancia del scanner. Las opciones se pueden encadenar aquí.
    val scanner: GmsBarcodeScanner = GmsBarcodeScanning.getClient(context)

    // Este es el "lanzador" que inicia el scanner y espera un resultado.
    // La librería ahora provee su propio `ActivityResultContract`.
    val scannerLauncher = rememberLauncherForActivityResult(contract = GmsBarcodeScanning.createActivityResultContract()) { result: Barcode? ->
        if (result != null) {
            scannedValue = result.rawValue
        } else {
            // El usuario podría haber cerrado el escáner sin escanear nada.
            // No es necesariamente un error, así que podrías no mostrar nada
            // o mantener el mensaje anterior.
        }
    }

    // Este es el "lanzador" que pide el permiso de la cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido, ahora sí lanzamos el scanner
            scannerLauncher.launch(null) // Lanzamos el escáner
        } else {
            // Permiso denegado, informamos al usuario
            Toast.makeText(context, "El permiso de la cámara es necesario para escanear.", Toast.LENGTH_LONG).show()
        }
    }
    // ---- FIN DE LO NUEVO ----

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
            // Botón modificado para lanzar el scanner
            Button(onClick = {
                // ---- LÓGICA DEL BOTÓN MODIFICADA ----
                // 1. Comprobar si ya tenemos el permiso
                val hasCameraPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasCameraPermission) {
                    // Si ya lo tenemos, lanzamos el scanner directamente
                    scannerLauncher.launch(null)
                } else {
                    // Si no lo tenemos, lo pedimos
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Escanear QR")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Texto para mostrar el resultado del escaneo
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