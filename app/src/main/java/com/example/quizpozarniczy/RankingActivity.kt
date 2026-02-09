package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)

        // 📊 SORTOWANIE WYNIKÓW
        val sorted = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        txtRanking.text = sorted.joinToString("\n") {
            "${it.playerName}: ${it.score} pkt, czas: ${formatTime(it.timeSeconds)}"
        }

        // 🔁 POWRÓT + RESET SESJI
        btnBack.setOnClickListener {

            QuizSession.resetAll()   // 🔥 NAJWAŻNIEJSZA LINIA

            val intent = Intent(this, JudgeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                           Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            finish()
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
