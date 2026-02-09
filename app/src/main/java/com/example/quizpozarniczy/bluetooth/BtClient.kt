package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.InputStream

class BtClient {

    fun connectAndReceive(
        device: BluetoothDevice,
        onDataReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val socket: BluetoothSocket =
                    device.createRfcommSocketToServiceRecord(
                        BtProtocol.SERVICE_UUID
                    )

                socket.connect()

                val input: InputStream = socket.inputStream
                val buffer = ByteArray(BtProtocol.BUFFER_SIZE)
                val sb = StringBuilder()

                var bytes: Int
                while (input.read(buffer).also { bytes = it } != -1) {
                    sb.append(String(buffer, 0, bytes))
                }

                socket.close()
                onDataReceived(sb.toString())

            } catch (e: Exception) {
                onError(e.message ?: "Błąd połączenia BT")
            }
        }.start()
    }
}
