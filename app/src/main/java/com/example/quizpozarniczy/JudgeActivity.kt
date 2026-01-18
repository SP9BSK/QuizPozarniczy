package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val edtPlayers = findViewById<EditText>(R.id.edtPlayers)
        val edtTime = findViewById<EditText>(R.id.edtTime)
        val edtQuestions = findViewById<EditText>(R.id.edtQuestions)
        val btnStart = findViewById<Button>(R.id.btnStartQuiz)

        btnStart.setOnClickListener {
            val players = edtPlayers.text.toString().toIntOrNull() ?: 1
            val time = edtTime.text.toString().toIntOrNull() ?: 10
            val questions = edtQuestions.text.toString().toIntOrNull() ?: 10

            QuizSession.reset()
            QuizSession.totalPlayers = players

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("TIME", time)
            intent.putExtra("QUESTIONS", questions)
            startActivity(intent)
        }
    }
}
