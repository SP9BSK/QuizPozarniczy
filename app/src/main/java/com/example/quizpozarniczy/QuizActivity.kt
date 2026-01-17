package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max

class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var index = 0
    private var score = 0
    private lateinit var timerText: TextView
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timeMinutes = intent.getIntExtra("TIME", 10)
        val totalMillis = max(1, timeMinutes) * 60 * 1000L

        timerText = findViewById(R.id.txtTimer)

        questions = QuizRepository.loadQuestions(this).shuffled()

        startTimer(totalMillis)
        showQuestion()
    }

    private fun startTimer(timeMillis: Long) {
        timer = object : CountDownTimer(timeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val min = seconds / 60
                val sec = seconds % 60
                timerText.text = String.format("Czas: %02d:%02d", min, sec)
            }

            override fun onFinish() {
                Toast.makeText(
                    this@QuizActivity,
                    "Czas minął! Wynik: $score",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }.start()
    }

    private fun showQuestion() {
        if (index >= questions.size) {
            timer.cancel()
            Toast.makeText(this, "Koniec! Wynik: $score", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val q = questions[index]

        findViewById<TextView>(R.id.txtQuestion).text = q.question

        val buttons = listOf(
            findViewById<Button>(R.id.btnA),
            findViewById<Button>(R.id.btnB),
            findViewById<Button>(R.id.btnC),
            findViewById<Button>(R.id.btnD)
        )

        for (i in buttons.indices) {
            buttons[i].text = q.answers[i]
            buttons[i].setOnClickListener {
                if (i == q.correctIndex) score++
                index++
                showQuestion()
            }
        }
    }

    override fun onBackPressed() {
        // cofanie zablokowane
    }
}
