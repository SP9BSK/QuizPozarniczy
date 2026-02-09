package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import kotlin.concurrent.thread

class BtClient(
    private val context: Context,
    private val qrData: String,
    private val onReceived: (String) -> Unit
) {

    private var running = true

    fun start() {
        thread {
            try {
                val (sessionId, deviceName) = qrData.split("|")
                val adapter = BluetoothAdapter.getDefaultAdapter()

                val device: BluetoothDevice = adapter.bondedDevices
                    .first { it.name == deviceName }

                val socket = device.createRfcommSocketToServiceRecord(
                    BtProtocol.APP_UUID
                )

                adapter.cancelDiscovery()
                socket.connect()

                val input = socket.inputStream
                val json = input.readBytes().toString(Charsets.UTF_8)

                if (running) onReceived(json)

                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stop() {
        running = false
    }
}
