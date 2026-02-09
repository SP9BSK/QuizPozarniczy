package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.graphics.Bitmap
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.util.QuizExporter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.concurrent.thread

object BtServer {

    private var serverSocket: BluetoothServerSocket? = null
    private var running = false
    private var questions: List<LocalQuestion> = emptyList()
    private var sessionId: String = ""

    fun startServer(context: Context, localQuestions: List<LocalQuestion>): String {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        require(adapter != null && adapter.isEnabled) { "Bluetooth wyłączony" }

        sessionId = System.currentTimeMillis().toString()
        questions = localQuestions
        running = true

        serverSocket = adapter.listenUsingRfcommWithServiceRecord(
            "QuizPozarniczy",
            BtProtocol.APP_UUID
        )

        thread {
            try {
                val socket = serverSocket!!.accept()
                sendQuestions(socket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return "${sessionId}|${adapter.name}"
    }

    private fun sendQuestions(socket: BluetoothSocket) {
        socket.use {
            val json = QuizExporter.localQuestionsToJson(questions)
            val out = it.outputStream
            out.write(json.toByteArray(Charsets.UTF_8))
            out.flush()
        }
    }

    fun generateQrForSession(data: String): Bitmap {
        val encoder = BarcodeEncoder()
        return encoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 800, 800)
    }

    fun stopServer() {
        running = false
        serverSocket?.close()
    }
}
