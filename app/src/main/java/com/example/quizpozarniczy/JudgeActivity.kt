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
        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {

            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questions = etQuestions.text.toString().toIntOrNull() ?: 1
            val minutes = etTime.text.toString().toIntOrNull() ?: 1

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", minutes * 60)

            startActivity(intent)

            // ❗❗❗ NIE DAJEMY finish() ❗❗❗
            // bo wtedy Android wraca do pierwszego ekranu
        }
    }
}
