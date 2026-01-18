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
        val pickerQuestions = findViewById<NumberPicker>(R.id.pickerQuestions)
        val pickerTime = findViewById<NumberPicker>(R.id.pickerTime)
        val btnStart = findViewById<Button>(R.id.btnStartQuiz)

        // konfiguracja pickerów
        pickerPlayers.minValue = 1
        pickerPlayers.maxValue = 5

        pickerQuestions.minValue = 5
        pickerQuestions.maxValue = 30

        pickerTime.minValue = 1
        pickerTime.maxValue = 30

        btnStart.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", pickerPlayers.value)
            intent.putExtra("QUESTIONS", pickerQuestions.value)
            intent.putExtra("TIME", pickerTime.value)
            startActivity(intent)
            finish()
        }
    }
}
