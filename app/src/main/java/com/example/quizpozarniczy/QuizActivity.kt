package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var txtTimer: TextView

    private val questions = QuizRepository.getQuestions()
    private var currentIndex = 0
    private var score = 0
    private var selectedAnswer = -1

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        txtTimer = findViewById(R.id.txtTimer)

        startTimer()
        showQuestion()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }
    }

    private fun showQuestion() {
        val q = questions[currentIndex]
        txtQuestion.text = q.question
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun answer(index: Int) {
        selectedAnswer = index

        // 🔥 TU JEST TEN FRAGMENT, O KTÓRY PYTAŁEŚ
        if (selectedAnswer == questions[currentIndex].correct) {
            score++
        }

        currentIndex++

        if (currentIndex < questions.size) {
            showQuestion()
        } else {
            endQuiz()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(30 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                txtTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        timer.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("total", questions.size)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // blokada cofania w trakcie quizu
    }
}
