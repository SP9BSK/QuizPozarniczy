package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerResultActivity : AppCompatActivity() {

    private lateinit var txtTitle: TextView
    private lateinit var txtScore: TextView
    private lateinit var txtTime: TextView
    private lateinit var btnShowAnswers: Button
    private lateinit var btnNext: Button

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_result)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtTitle = findViewById(R.id.txtTitle)
        txtScore = findViewById(R.id.txtScore)
        txtTime = findViewById(R.id.txtTime)
        btnShowAnswers = findViewById(R.id.btnShowAnswers)
        btnNext = findViewById(R.id.btnNext)

        currentIndex = intent.getIntExtra("PLAYER_INDEX", 0)

        val result = QuizSession.results[currentIndex]

        txtTitle.text = "Zawodnik ${result.playerNumber}: ${result.playerName}"
        txtScore.text =
            "Wynik: ${result.score} / ${result.total} (${result.percentScore}%)"
        txtTime.text =
            "Czas: ${formatTime(result.timeSeconds)}"

        // 🔹 Pokaż dobre odpowiedzi – tylko jeśli są błędy
        btnShowAnswers.isEnabled = result.hasWrongAnswers
        btnShowAnswers.setOnClickListener {
            val i = Intent(this, WrongAnswersActivity::class.java)
            i.putExtra("PLAYER_INDEX", currentIndex)
            startActivity(i)
        }

        // 🔹 Następny zawodnik / Wyniki końcowe
        if (currentIndex < QuizSession.results.size - 1) {
            btnNext.text = "Następny zawodnik"
            btnNext.setOnClickListener {
                val i = Intent(this, PlayerResultActivity::class.java)
                i.putExtra("PLAYER_INDEX", currentIndex + 1)
                startActivity(i)
                finish()
            }
        } else {
            btnNext.text = "Pokaż wyniki końcowe"
            btnNext.setOnClickListener {
                startActivity(Intent(this, ResultActivity::class.java))
                finish()
            }
        }
    }

    private fun formatTime(seconds: Int): String =
        String.format("%02d:%02d", seconds / 60, seconds % 60)
}
