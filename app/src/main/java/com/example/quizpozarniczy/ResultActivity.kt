package com.example.quizpozarniczy

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)

        txtRanking.typeface = Typeface.MONOSPACE

        // 🔒 Zabezpieczenie przed pustą listą
        if (QuizSession.results.isEmpty()) {
            txtRanking.text = "Brak wyników do wyświetlenia."
            btnBack.setOnClickListener {
                QuizSession.resetTournament() // pełny reset zawodników i wyników
                val intent = Intent(this, JudgeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            return
        }

        // 🔥 Sortowanie: najpierw punkty malejąco, potem czas rosnąco
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
                    "%-3d %-15s %7s %6s\n",
                    index + 1,
                    r.playerName.take(15),
                    "${r.score}/${r.total}",
                    formatTime(r.timeSeconds)
                )
            )
        }

        txtRanking.text = sb.toString()

        btnBack.setOnClickListener {
            // 🔹 Pełny reset turnieju (czyści też nazwy zawodników)
            QuizSession.resetTournament()
            val intent = Intent(this, JudgeActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
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
