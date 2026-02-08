package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        val resultsText = intent.getStringExtra("RESULTS") ?: ""
        val timeMillis = intent.getLongExtra("TIME_MILLIS", 0L)

        val timeFormatted = formatTime(timeMillis)

        findViewById<TextView>(R.id.txtResult).text =
            "Wynik: $score / $total\nCzas: $timeFormatted"

        val btnBack = findViewById<Button>(R.id.btnBackToJudge)
        val btnShowCorrect = findViewById<Button>(R.id.btnShowCorrect)

        // 🔥 JEŚLI SĄ BŁĘDY → POKAŻ PRZYCISK
        if (score < total) {
            btnShowCorrect.visibility = Button.VISIBLE
        }

        btnShowCorrect.setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("RESULTS", resultsText)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, JudgeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, JudgeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
