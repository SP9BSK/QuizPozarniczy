package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val pickerPlayers = findViewById<NumberPicker>(R.id.pickerPlayers)
        val pickerTime = findViewById<NumberPicker>(R.id.pickerTime)
        val btnStart = findViewById<Button>(R.id.btnStartQuiz)

        // 1–5 zawodników
        pickerPlayers.minValue = 1
        pickerPlayers.maxValue = 5
        pickerPlayers.value = 1

        // 1–30 minut
        pickerTime.minValue = 1
        pickerTime.maxValue = 30
        pickerTime.value = 10

        btnStart.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS_COUNT", pickerPlayers.value)
            intent.putExtra("QUIZ_TIME", pickerTime.value)
            startActivity(intent)
        }
    }
}
