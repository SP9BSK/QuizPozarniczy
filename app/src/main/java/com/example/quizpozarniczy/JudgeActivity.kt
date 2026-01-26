package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
        private const val MAX_TIME_MINUTES = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        // 🔒 ekran zawsze włączony
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {

            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questions = etQuestions.text.toString().toIntOrNull() ?: 1
            val minutes = etTime.text.toString().toIntOrNull() ?: 1

            if (players > MAX_PLAYERS || questions > MAX_QUESTIONS || minutes > MAX_TIME_MINUTES) {
                Toast.makeText(
                    this,
                    "Przekroczono dopuszczalne wartości",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", minutes * 60)

            startActivity(intent)

            // ❗❗❗ NIE WOLNO TU DAWAĆ finish() ❗❗❗
        }
    }
}
