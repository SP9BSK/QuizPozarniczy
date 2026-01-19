package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var txtResult: TextView

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private var currentQuestionIndex = 0
    private var points = 0

    // TYMCZASOWE pytania – prawdziwe podmienimy później
    private val questions = listOf(
        Pair(
            "Jakie napięcie ma standardowe gniazdko w Polsce?",
            listOf("110V", "220V", "230V", "400V")
        ),
        Pair(
            "Jaki numer alarmowy ma straż pożarna?",
            listOf("997", "998", "999", "112")
        )
    )

    private val correctAnswers = listOf(2, 1) // index poprawnej odpowiedzi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtTimer = findViewById(R.id.txtTimer)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtResult = findViewById(R.id.txtResult)

        btnA = findViewById(R.id.btnAnswerA)
        btnB = findViewById(R.id.btnAnswerB)
        btnC = findViewById(R.id.btnAnswerC)
        btnD = findViewById(R.id.btnAnswerD)
        btnBack = findViewById(R.id.btnBack)

        showQuestion()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResult()
            return
        }

        val question = questions[currentQuestionIndex]
        txtQuestion.text = question.first

        btnA.text = "A. ${question.second[0]}"
        btnB.text = "B. ${question.second[1]}"
        btnC.text = "C. ${question.second[2]}"
        btnD.text = "D. ${question.second[3]}"
    }

    private fun answer(selectedIndex: Int) {
        if (selectedIndex == correctAnswers[currentQuestionIndex]) {
            points++
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun showResult() {
        txtQuestion.text = "Koniec quizu"
        txtResult.text = "Wynik: $points / ${questions.size}"

        btnA.isEnabled = false
        btnB.isEnabled = false
        btnC.isEnabled = false
        btnD.isEnabled = false

        btnBack.visibility = Button.VISIBLE
    }
}
