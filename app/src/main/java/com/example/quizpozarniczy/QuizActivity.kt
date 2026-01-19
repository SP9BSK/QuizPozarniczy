package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private val questions = QuizRepository.getQuestions()
    private var current = 0
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        val timeSeconds = intent.getIntExtra("TIME", 60)

        startTimer(timeSeconds)
        showQuestion()

        val click = { _: Int ->
            current++
            if (current < questions.size) {
                showQuestion()
            } else {
                endQuiz()
            }
        }

        btnA.setOnClickListener { click(0) }
        btnB.setOnClickListener { click(1) }
        btnC.setOnClickListener { click(2) }
        btnD.setOnClickListener { click(3) }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showQuestion() {
        val q = questions[current]
        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                txtTimer.text = "Czas: ${sec}s"
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        timer.cancel()

        txtQuestion.text = "Koniec quizu"

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
    }
}
