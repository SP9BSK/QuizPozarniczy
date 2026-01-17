package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val inputQuestions = findViewById<EditText>(R.id.inputQuestions)
        val inputTime = findViewById<EditText>(R.id.inputTime)
        val inputPlayers = findViewById<EditText>(R.id.inputPlayers)
        val startButton = findViewById<Button>(R.id.btnStartQuiz)

        startButton.setOnClickListener {

            val questions = inputQuestions.text.toString().toIntOrNull() ?: 0
            val time = inputTime.text.toString().toIntOrNull() ?: 0
            val players = inputPlayers.text.toString().toIntOrNull() ?: 0

            if (questions !in 1..30 || time !in 1..30 || players !in 1..5) {
                Toast.makeText(
                    this,
                    "Błędne dane (pytania ≤30, czas ≤30, zawodnicy ≤5)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME", time)
            intent.putExtra("PLAYERS", players)
            startActivity(intent)
        }
    }
}
