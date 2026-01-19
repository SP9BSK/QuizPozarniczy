package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Player

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private val questions = QuizRepository.getQuestions()
    private var currentQuestion = 0
    private var currentPlayer = 0
    private lateinit var players: List<Player>
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

        val playerCount = intent.getIntExtra("PLAYERS", 1)
        val timeSeconds = intent.getIntExtra("TIME", 60)

        players = List(playerCount) { Player(it + 1) }

        startTimer(timeSeconds)
        showQuestion()

        val answerClick = { index: Int ->
            val q = questions[currentQuestion]
            if (index == q.correctIndex) {
                players[currentPlayer].points++
            }

            currentPlayer = (currentPlayer + 1) % players.size
            currentQuestion++

            if (currentQuestion < questions.size) {
                showQuestion()
            } else {
                endQuiz()
            }
        }

        btnA.setOnClickListener { answerClick(0) }
        btnB.setOnClickListener { answerClick(1) }
        btnC.setOnClickListener { answerClick(2) }
        btnD.setOnClickListener { answerClick(3) }
    }

    private fun showQuestion() {
        val q = questions[currentQuestion]
        txtQuestion.text =
            "Zawodnik ${players[currentPlayer].id}\n\n${q.text}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtTimer.text = "Czas: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        timer.cancel()

        val resultText = players.joinToString("\n") {
            "Zawodnik ${it.id}: ${it.points} pkt"
        }

        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("RESULTS", resultText)
        startActivity(intent)
        finish()
    }
}
