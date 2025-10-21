package com.utch.wear.presentation

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

/**
 * Servicio que escucha mensajes del teléfono incluso cuando la app está en segundo plano
 */
class WearMessageListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("VendetaWear", "📨 Servicio recibió mensaje: ${messageEvent.path}")

        if (messageEvent.path == "/game_result") {
            val result = String(messageEvent.data)
            Log.d("VendetaWear", "📦 Resultado: $result")

            // Enviar broadcast a la Activity
            val intent = Intent("com.utch.wear.GAME_RESULT")
            intent.putExtra("result", result)
            sendBroadcast(intent)

            Log.d("VendetaWear", "📡 Broadcast enviado a MainActivity")
        }
    }
}