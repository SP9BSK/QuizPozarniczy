package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import com.example.quizpozarniczy.util.QuizImporter
import java.io.InputStream
import java.util.*
import android.os.Handler
import android.os.Looper

class BtClient(
    private val deviceName: String,
    private val uuidString: String,
    private val callback: (String) -> Unit
) : Thread() {

    private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun run() {
        try {
            val device = adapter.bondedDevices.firstOrNull { it.name == deviceName } ?: return
            val uuid = UUID.fromString(uuidString)
            val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
            val inputStream: InputStream = socket.inputStream
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            socket.close()

            Handler(Looper.getMainLooper()).post {
                callback(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
