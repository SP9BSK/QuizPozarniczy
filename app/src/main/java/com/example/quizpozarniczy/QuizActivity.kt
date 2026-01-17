package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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

    private val questions = QuizRepository.getQuestions()
    private var currentIndex = 0
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        startTimer()
        showQuestion()

        val buttons = listOf(btnA, btnB, btnC, btnD)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index)
            }
        }
    }

    private fun showQuestion() {
        if (currentIndex >= questions.size) {
            finishQuiz()
            return
        }

        val q = questions[currentIndex]
        txtQuestion.text = q.text

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun checkAnswer(selected: Int) {
        if (questions[currentIndex].correct == selected) {
            correctAnswers++
        }
        currentIndex++
        showQuestion()
    }

    private fun startTimer() {
        object : CountDownTimer(30 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = millisUntilFinished / 1000 / 60
                val sec = (millisUntilFinished / 1000) % 60
                txtTimer.text = String.format("%02d:%02d", min, sec)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    private fun finishQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", correctAnswers)
        intent.putExtra("total", questions.size)
        startActivity(intent)
        finish()
    }
}
