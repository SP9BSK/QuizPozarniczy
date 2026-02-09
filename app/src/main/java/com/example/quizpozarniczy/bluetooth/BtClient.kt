package com.example.quizpozarniczy.bluetooth

import android.content.Context
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.util.QuizExporter
import kotlin.concurrent.thread

class BtClient(
    private val context: Context,
    private val sessionId: String,
    private val onReceived: (String) -> Unit
) {

    private var running = true

    fun start() {
        // Uruchamiamy w tle symulację pobierania pytań
        thread {
            // Symulacja połączenia po sesji Bluetooth
            Thread.sleep(1000) // symulacja opóźnienia
            val questions = BtServer.getQuestionsForSession(sessionId)
            val json = QuizExporter.localQuestionsToJson(questions)
            if (running) onReceived(json)
        }
    }

    fun stop() {
        running = false
    }
}
