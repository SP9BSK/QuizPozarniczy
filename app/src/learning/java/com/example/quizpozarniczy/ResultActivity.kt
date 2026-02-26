package com.example.quizpozarniczy

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)

        txtRanking.typeface = Typeface.MONOSPACE

        // Zabezpieczenie
        if (QuizSession.results.isEmpty()) {
            txtRanking.text = "Brak wyniku do wyświetlenia."
            btnBack.setOnClickListener {
                QuizSession.resetTournament()
                val intent = Intent(this, StartActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            return
        }

        // Pobieramy wynik ostatniego zawodnika
        val result = QuizSession.results.last()

        // Tekst wyniku
        val wynikTekst = """
            Zawodnik: ${result.playerName}
            Punkty:   ${result.score}/${result.total}
            Czas:     ${formatTime(result.timeSeconds)}
        """.trimIndent()

        // JSON do QR
        val json = JSONObject().apply {
            put("player", result.playerName)
            put("score", result.score)
            put("total", result.total)
            put("time", result.timeSeconds)
        }

        val qrData = json.toString()

        // Wyświetlamy wynik + QR
        txtRanking.text = wynikTekst + "\n\nKod QR:\n$qrData"

        btnBack.setOnClickListener {
            QuizSession.resetTournament()
            val intent = Intent(this, StartActivity::class.java)
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
