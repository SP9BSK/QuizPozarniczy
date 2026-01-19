package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button
    private lateinit var txtResult: TextView
    private lateinit var btnBack: Button

    private val questions = listOf(
        "Czy ogień potrzebuje tlenu?",
        "Czy woda gasi wszystkie pożary?",
        "Czy dym jest niebezpieczny?"
    )

    private var currentQuestion = 0
    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // POWIĄZANIE Z XML
        txtTimer = findViewById(R.id.txtTimer)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnYes = findViewById(R.id.btnYes)
        btnNo = findViewById(R.id.btnNo)
        txtResult = findViewById(R.id.txtResult)
        btnBack = findViewById(R.id.btnBack)

        showQuestion()

        btnYes.setOnClickListener {
            points++
            nextQuestion()
        }

        btnNo.setOnClickListener {
            nextQuestion()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showQuestion() {
        txtQuestion.text = questions[currentQuestion]
    }

    private fun nextQuestion() {
        currentQuestion++

        if (currentQuestion < questions.size) {
            showQuestion()
        } else {
            endQuiz()
        }
    }

    private fun endQuiz() {
        txtQuestion.text = "Koniec quizu"
        txtResult.text = "Twój wynik: $points / ${questions.size}"
        btnYes.isEnabled = false
        btnNo.isEnabled = false
        btnBack.visibility = Button.VISIBLE
    }
}
