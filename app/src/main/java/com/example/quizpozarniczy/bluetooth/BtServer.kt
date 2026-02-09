package com.example.quizpozarniczy.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.OutputStream

class BtServer {

    private var serverSocket: BluetoothServerSocket? = null

    fun startServer(
        adapter: BluetoothAdapter,
        dataToSend: String,
        onClientConnected: () -> Unit,
        onFinished: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(
                BtProtocol.SERVICE_NAME,
                BtProtocol.SERVICE_UUID
            )

            Thread {
                try {
                    val socket: BluetoothSocket = serverSocket!!.accept()
                    onClientConnected()

                    val output: OutputStream = socket.outputStream
                    output.write(dataToSend.toByteArray(Charsets.UTF_8))
                    output.flush()

                    socket.close()
                    serverSocket?.close()
                    onFinished()

                } catch (e: Exception) {
                    onError(e.message ?: "Błąd połączenia")
                }
            }.start()

        } catch (e: Exception) {
            onError(e.message ?: "Nie można uruchomić serwera BT")
        }
    }

    fun stopServer() {
        try {
            serverSocket?.close()
        } catch (_: Exception) {}
    }
}
