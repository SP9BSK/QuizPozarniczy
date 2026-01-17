package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var index = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questions = QuizRepository.loadQuestions(this).shuffled()

        showQuestion()
    }

    private fun showQuestion() {
        if (index >= questions.size) {
            Toast.makeText(this, "Koniec! Wynik: $score", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val q = questions[index]

        findViewById<TextView>(R.id.txtQuestion).text = q.question

        val buttons = listOf(
            findViewById<Button>(R.id.btnA),
            findViewById<Button>(R.id.btnB),
            findViewById<Button>(R.id.btnC),
            findViewById<Button>(R.id.btnD)
        )

        for (i in buttons.indices) {
            buttons[i].text = q.answers[i]
            buttons[i].setOnClickListener {
                if (i == q.correctIndex) score++
                index++
                showQuestion()
            }
        }
    }

    override fun onBackPressed() {
        // blokada cofania
    }
}
