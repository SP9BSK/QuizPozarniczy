package com.example.quizpozarniczy

import android.content.Intent
import android.graphics.Typeface
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

        // MONOSPACE = równe znaki
        txtRanking.typeface = Typeface.MONOSPACE

        val sorted = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        val sb = StringBuilder()

        // NAGŁÓWEK
        sb.append(
            String.format(
                "%-3s %-15s %5s %6s\n",
                "M.",
                "ZAWODNIK",
                "PKT",
                "CZAS"
            )
        )
        sb.append("----------------------------------------\n")

        // DANE
        sorted.forEachIndexed { index, r ->
            sb.append(
                String.format(
                    "%-3d %-15s %5d %6s\n",
                    index + 1,
                    r.playerName.take(15),
                    r.score,
                    formatTime(r.timeSeconds)
                )
            )
        }

        txtRanking.text = sb.toString()

        btnBack.setOnClickListener {
            QuizSession.resetAll()

            val intent = Intent(this, JudgeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
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
