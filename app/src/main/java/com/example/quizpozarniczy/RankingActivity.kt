 package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)

        // Sortowanie: punkty malejąco, jeśli punkty równe → czas rosnąco
        val sorted = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        val text = sorted.joinToString("\n") {
            "Zawodnik ${it.playerNumber}: ${it.score} pkt, czas: ${formatTime(it.timeSeconds)}"
        }

        txtRanking.text = text
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
