package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Question

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private val questions: List<Question> = QuizRepository.getQuestions()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        showQuestion()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }
    }

    private fun showQuestion() {
        val q = questions[currentIndex]
        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun answer(index: Int) {
        currentIndex++
        if (currentIndex < questions.size) {
            showQuestion()
        } else {
            finish() // wraca do panelu sędziego
        }
    }
}
