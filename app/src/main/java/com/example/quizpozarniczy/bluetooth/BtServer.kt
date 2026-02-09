package com.example.quizpozarniczy.bluetooth

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.example.quizpozarniczy.model.LocalQuestion
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object BtServer {

    private var sessionId: String? = null
    private var questions: List<LocalQuestion> = emptyList()

    fun startServer(context: Context, localQuestions: List<LocalQuestion>): String {
        // Generujemy losowy identyfikator sesji
        sessionId = System.currentTimeMillis().toString()
        questions = localQuestions
        // Tutaj powinien być kod uruchamiający actual BluetoothServerSocket
        return sessionId!!
    }

    fun generateQrForSession(sessionId: String): Bitmap {
        val encoder = BarcodeEncoder()
        return encoder.encodeBitmap(sessionId, BarcodeFormat.QR_CODE, 800, 800)
    }

    fun stopServer() {
        // Tutaj zatrzymujemy serwer Bluetooth jeśli był uruchomiony
    }

    fun getQuestionsForSession(sessionId: String): List<LocalQuestion> {
        return if (sessionId == this.sessionId) questions else emptyList()
    }
}
