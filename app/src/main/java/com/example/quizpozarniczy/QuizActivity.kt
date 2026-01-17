package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private var index = 0
    private var score = 0

    private var playerIndex = 1
    private var playersCount = 1
    private val results = mutableListOf<Int>()

    private lateinit var timer: CountDownTimer

    private var questions = QuizRepository.getQuestions().shuffled()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        // ⬇⬇⬇ TO JEST KROK 7.2
        playersCount = intent.getIntExtra("PLAYERS", 1)
        val timeMinutes = intent.getIntExtra("TIME", 30)

        startTimer(timeMinutes)
        showQuestion()
    }

    private fun startTimer(minutes: Int) {
        timer = object : CountDownTimer(minutes * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                txtTimer.text = "Zawodnik $playerIndex / $playersCount | Czas: ${sec}s"
            }

            override fun onFinish() {
                Toast.makeText(this@QuizActivity, "Koniec czasu!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        timer.start()
    }

    private fun showQuestion() {
        if (index >= questions.size) {
            results.add(score)

            if (playerIndex < playersCount) {
                playerIndex++
                index = 0
                score = 0
                questions = QuizRepository.getQuestions().shuffled()
                Toast.makeText(
                    this,
                    "Zawodnik ${playerIndex - 1} zakończył",
                    Toast.LENGTH_SHORT
                ).show()
                showQuestion()
            } else {
                timer.cancel()
                Toast.makeText(this, "Quiz zakończony", Toast.LENGTH_LONG).show()
                finish()
            }
            return
        }

        val q = questions[index]
        txtQuestion.text = q.question
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]

        val click = { i: Int ->
            if (i == q.correct) score++
            index++
            showQuestion()
        }

        btnA.setOnClickListener { click(0) }
        btnB.setOnClickListener { click(1) }
        btnC.setOnClickListener { click(2) }
        btnD.setOnClickListener { click(3) }
    }
}
