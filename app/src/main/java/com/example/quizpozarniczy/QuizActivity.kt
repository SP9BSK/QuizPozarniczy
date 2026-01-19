package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private val questions = QuizRepository.getQuestions()
    private var current = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        showQuestion()

        val click = { index: Int ->
            current++
            if (current < questions.size) showQuestion()
            else txtQuestion.text = "Koniec quizu"
        }

        btnA.setOnClickListener { click(0) }
        btnB.setOnClickListener { click(1) }
        btnC.setOnClickListener { click(2) }
        btnD.setOnClickListener { click(3) }
    }

    private fun showQuestion() {
        val q = questions[current]
        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }
}
