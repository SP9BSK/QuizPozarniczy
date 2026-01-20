package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Player
import com.example.quizpozarniczy.model.Question

class QuizActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var questionText: TextView
    private lateinit var answers: List<Button>

    private var currentQuestion = 0
    private var currentPlayer = 0

    private lateinit var players: List<Player>
    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        timerText = findViewById(R.id.txtTimer)
        questionText = findViewById(R.id.txtQuestion)

        answers = listOf(
            findViewById(R.id.btnA),
            findViewById(R.id.btnB),
            findViewById(R.id.btnC),
            findViewById(R.id.btnD)
        )

        val playerCount = intent.getIntExtra("players", 1)
        val timeMinutes = intent.getIntExtra("time", 1)

        players = List(playerCount) { Player(it + 1) }
        questions = QuizRepository.getQuestions()

        startTimer(timeMinutes * 60 * 1000L)
        showQuestion()
    }

    private fun startTimer(ms: Long) {
        object : CountDownTimer(ms, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                timerText.text = "Czas: ${sec / 60}:${sec % 60}"
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentQuestion >= questions.size) {
            endQuiz()
            return
        }

        val q = questions[currentQuestion]
        questionText.text = "Zawodnik ${players[currentPlayer].id}: ${q.text}"

        answers.forEachIndexed { index, button ->
            button.text = q.answers[index]
            button.setOnClickListener {
                if (index == q.correctIndex) {
                    players[currentPlayer].points++
                }
                currentPlayer = (currentPlayer + 1) % players.size
                currentQuestion++
                showQuestion()
            }
        }
    }

    private fun endQuiz() {
        questionText.text = "KONIEC QUIZU"
    }
}
