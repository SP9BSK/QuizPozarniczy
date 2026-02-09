package com.example.quizpozarniczy

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        txtRanking.typeface = Typeface.MONOSPACE

        // Sortujemy wyniki z QuizSession
        val results = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        val sb = StringBuilder()
        sb.append(
            String.format(
                "%-4s %-15s %6s %8s\n",
                "M", "ZAWODNIK", "PKT", "CZAS"
            )
        )
        sb.append("----------------------------------------\n")

        results.forEachIndexed { index, result ->
            sb.append(
                String.format(
                    "%-4d %-15s %6d %8s\n",
                    index + 1,
                    result.playerName.take(15),
                    result.score,
                    formatTime(result.timeSeconds)
                )
            )
        }

        txtRanking.text = sb.toString()
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
