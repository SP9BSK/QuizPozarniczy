package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button

    private val questions = listOf(
        "Czy woda nadaje się do gaszenia pożaru oleju?",
        "Czy strażak musi nosić hełm podczas akcji?",
        "Czy gaśnica proszkowa nadaje się do gaszenia instalacji elektrycznych?"
    )

    private val answers = listOf(
        false,
        true,
        true
    )

    private var index = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        btnYes = findViewById(R.id.btnYes)
        btnNo = findViewById(R.id.btnNo)

        showQuestion()

        btnYes.setOnClickListener { checkAnswer(true) }
        btnNo.setOnClickListener { checkAnswer(false) }
    }

    private fun showQuestion() {
        if (index < questions.size) {
            txtQuestion.text = questions[index]
        } else {
            txtQuestion.text = "Koniec quizu\nWynik: $score / ${questions.size}"
            btnYes.isEnabled = false
            btnNo.isEnabled = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (answers[index] == userAnswer) {
            score++
        }
        index++
        showQuestion()
    }
}
