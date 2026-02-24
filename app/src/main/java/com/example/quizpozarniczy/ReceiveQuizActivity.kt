package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import org.json.JSONObject

class ReceiveQuizActivity : AppCompatActivity() {

    private lateinit var txtInfo: TextView
    private lateinit var btnScan: Button
    private lateinit var btnStart: Button

    private var timeSeconds = 60
    private var ids: List<Int> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_quiz)

        txtInfo = findViewById(R.id.txtInfo)
        btnScan = findViewById(R.id.btnScan)
        btnStart = findViewById(R.id.btnStart)

        btnStart.isEnabled = false

        btnScan.setOnClickListener {
            startQrScan()
        }

        btnStart.setOnClickListener {
            startQuiz()
        }
    }

    private fun startQrScan() {
        // Tu wywołujesz skaner QR (ZXing lub MLKit)
        // Po zeskanowaniu wywołujesz:
        // onQrScanned(resultText)
    }

    private fun onQrScanned(text: String) {
        val json = JSONObject(text)

        timeSeconds = json.getInt("time")

        val arr = json.getJSONArray("ids")
        ids = List(arr.length()) { i -> arr.getInt(i) }

        txtInfo.text = "Pobrano quiz:\n" +
                "• Pytań: ${ids.size}\n" +
                "• Czas: $timeSeconds sekund"

        btnStart.isEnabled = true
    }

    private fun startQuiz() {
        val all = LocalQuestionsRepository().getAllQuestions()
        val selected = all.filter { it.id in ids }

        QuizSession.questions = selected
        QuizSession.currentPlayer = 1
        QuizSession.totalPlayers = 1
        QuizSession.playerNames = listOf("Zawodnik")
        QuizSession.results.clear()

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("TIME_SECONDS", timeSeconds)
        intent.putExtra("QUESTIONS", selected.size)
        startActivity(intent)
        finish()
    }
}
