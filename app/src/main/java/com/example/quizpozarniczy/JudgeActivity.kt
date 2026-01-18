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

        val edtPlayers = findViewById<EditText>(R.id.edtPlayers)
        val edtQuestions = findViewById<EditText>(R.id.edtQuestions)
        val edtTime = findViewById<EditText>(R.id.edtTime)
        val btnStart = findViewById<Button>(R.id.btnStartQuiz)

        btnStart.setOnClickListener {

            val players = edtPlayers.text.toString().toIntOrNull()
            val questions = edtQuestions.text.toString().toIntOrNull()
            val time = edtTime.text.toString().toIntOrNull()

            if (players == null || players !in 1..5) {
                toast("Liczba zawodników: 1–5")
                return@setOnClickListener
            }

            if (questions == null || questions !in 5..30) {
                toast("Liczba pytań: 5–30")
                return@setOnClickListener
            }

            if (time == null || time !in 1..30) {
                toast("Czas: 1–30 minut")
                return@setOnClickListener
            }

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME", time)

            startActivity(intent)
            finish()
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
