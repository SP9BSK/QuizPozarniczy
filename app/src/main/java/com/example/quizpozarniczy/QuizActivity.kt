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

    private lateinit var questions: List<Question>
    private var index = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtTimer = findViewById(R.id.txtTimer)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        val timeMinutes = intent.getIntExtra("TIME", 10)
        val totalTime = timeMinutes * 60 * 1000L

        questions = QuizRepository.getQuestions()

        showQuestion()

        object : CountDownTimer(totalTime, 1000) {
            override fun onTick(ms: Long) {
                val m = ms / 60000
                val s = (ms % 60000) / 1000
                txtTimer.text = "$m:${s.toString().padStart(2, '0')}"
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }
    }

    private fun showQuestion() {
        val q = questions[index]
        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun answer(selected: Int) {
        if (selected == questions[index].correctIndex) {
            score++
        }

        index++
        if (index < questions.size) {
            showQuestion()
        } else {
            endQuiz()
        }
    }

    private fun endQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL", questions.size)
        startActivity(intent)
        finish()
    }
}
