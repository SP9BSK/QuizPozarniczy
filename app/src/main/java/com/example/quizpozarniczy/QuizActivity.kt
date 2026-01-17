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

    private var currentIndex = 0
    private var score = 0

    private lateinit var questions: List<Question>
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

        val timeMinutes = intent.getIntExtra("time", 30)
        val questionCount = intent.getIntExtra("questions", 30)

        questions = QuizRepository.getQuestions().take(questionCount)

        startTimer(timeMinutes)
        showQuestion()

        btnA.setOnClickListener { checkAnswer(0) }
        btnB.setOnClickListener { checkAnswer(1) }
        btnC.setOnClickListener { checkAnswer(2) }
        btnD.setOnClickListener { checkAnswer(3) }
    }

    private fun startTimer(minutes: Int) {
        timer = object : CountDownTimer(minutes * 60 * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val min = ms / 1000 / 60
                val sec = (ms / 1000) % 60
                txtTimer.text = String.format("%02d:%02d", min, sec)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
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

    private fun checkAnswer(answerIndex: Int) {
        if (questions[currentIndex].correct == answerIndex) {
            score++
        }
        currentIndex++
        showQuestion()
    }

    private fun finishQuiz() {
        timer.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("max", questions.size)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // blokada cofania
    }
}
