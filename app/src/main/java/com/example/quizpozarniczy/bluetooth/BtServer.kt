package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.util.QuizExporter
import java.io.OutputStream
import java.util.*

class BtServer(private val localQuestions: List<LocalQuestion>) : Thread() {

    companion object {
        val SERVICE_UUID: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
        fun getDeviceName(): String = BluetoothAdapter.getDefaultAdapter().name ?: "QuizMDP_Opiekun"
    }

    private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun run() {
        try {
            val serverSocket: BluetoothServerSocket =
                adapter.listenUsingRfcommWithServiceRecord("QuizMDP", SERVICE_UUID)

            val socket: BluetoothSocket = serverSocket.accept()
            val outStream: OutputStream = socket.outputStream

            val json = QuizExporter.localQuestionsToJson(localQuestions)
            outStream.write(json.toByteArray())
            outStream.flush()
            outStream.close()
            socket.close()
            serverSocket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
