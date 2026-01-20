package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Player

class QuizActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var txtPlayer: TextView
    private lateinit var txtResult: TextView

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private val questions = QuizRepository.questions
    private var questionIndex = 0
    private var questionsLimit = 0

    private lateinit var players: MutableList<Player>
    private var currentPlayerIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtTimer = findViewById(R.id.txtTimer)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtPlayer = findViewById(R.id.txtPlayer)
        txtResult = findViewById(R.id.txtResult)

        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        val playersCount = intent.getIntExtra("PLAYERS", 1)
        questionsLimit = intent.getIntExtra("QUESTIONS", questions.size)
        val timeMinutes = intent.getIntExtra("TIME_MINUTES", 1)

        players = MutableList(playersCount) { index ->
            Player(id = index + 1)
        }

        startTimer(timeMinutes)
        showQuestion()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startTimer(minutes: Int) {
        val totalMillis = minutes * 60 * 1000L

        object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = millisUntilFinished / 60000
                val sec = (millisUntilFinished % 60000) / 1000
                txtTimer.text = String.format("Czas: %02d:%02d", min, sec)
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun showQuestion() {
        if (questionIndex >= questionsLimit) {
            endQuiz()
            return
        }

        val q = questions[questionIndex]

        txtQuestion.text = q.text
        txtPlayer.text = "Zawodnik ${players[currentPlayerIndex].id}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun answer(index: Int) {
        val q = questions[questionIndex]

        if (index == q.correctIndex) {
            players[currentPlayerIndex].points++
        }

        questionIndex++
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size

        showQuestion()
    }

    private fun endQuiz() {
        btnA.isEnabled = false
        btnB.isEnabled = false
        btnC.isEnabled = false
        btnD.isEnabled = false

        val resultText = StringBuilder("Koniec quizu\n\n")
        players.forEach {
            resultText.append("Zawodnik ${it.id}: ${it.points} pkt\n")
        }

        txtResult.text = resultText.toString()
        btnBack.visibility = Button.VISIBLE
    }
}
