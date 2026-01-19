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

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStartQuiz)

        btnStart.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questions = etQuestions.text.toString().toIntOrNull() ?: 1
            val time = etTime.text.toString().toIntOrNull() ?: 10

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS_COUNT", players)
            intent.putExtra("QUESTIONS_COUNT", questions)
            intent.putExtra("QUIZ_TIME", time)

            startActivity(intent)
        }
    }
}
