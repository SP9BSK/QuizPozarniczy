package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // ODBIÓR DANYCH Z PANELU SĘDZIEGO
        val questionsCount = intent.getIntExtra("QUESTIONS", 10)
        val timeMinutes = intent.getIntExtra("TIME", 10)
        val playersCount = intent.getIntExtra("PLAYERS", 1)

        val infoText = findViewById<TextView>(R.id.quizInfo)
        val endButton = findViewById<Button>(R.id.btnEndQuiz)

        infoText.text =
            "Pytania: $questionsCount\nCzas: $timeMinutes minut\nZawodnicy: $playersCount"

        endButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // BLOKADA COFANIA
    override fun onBackPressed() {
        // NIC – cofanie zablokowane
    }
}
