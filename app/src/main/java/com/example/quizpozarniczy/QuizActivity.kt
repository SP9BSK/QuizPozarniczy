package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Question
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private var questions: List<Question> = emptyList()
    private var currentPlayer = 0
    private var currentQuestionIndex = 0
    private var timer: CountDownTimer? = null
    private var playersCount = 1
    private lateinit var scores: IntArray
    private var questionsPerPlayer = 0
    private var timePerPlayerSeconds = 60


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        val questionsLimit = intent.getIntExtra("QUESTIONS", 1)
        val timeSeconds = intent.getIntExtra("TIME_SECONDS", 60)
        playersCount = min(intent.getIntExtra("PLAYERS", 1), 10)

        scores = IntArray(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled()
            .take(min(questionsLimit, allQuestions.size))

        questionsPerPlayer = questions.size

        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        showQuestion()

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnD.setOnClickListener { answerSelected(3) }

        btnBack.setOnClickListener { finish() }
    }

    private fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val min = ms / 1000 / 60
                val sec = (ms / 1000) % 60
                txtTimer.text = String.format("Czas: %02d:%02d", min, sec)
            }

            override fun onFinish() {
                showPlayerResult()

            }
        }.start()
    }

    private fun showQuestion() {

    if (currentQuestionIndex == 0) {
        startTimer(timePerPlayerSeconds)
    }

    if (currentQuestionIndex >= questions.size) {
        showPlayerResult()
        return
    }

    val q = questions[currentQuestionIndex]
    txtQuestion.text = "Zawodnik ${currentPlayer + 1}\n\n${q.text}"

    btnA.text = q.answers[0]
    btnB.text = q.answers[1]
    btnC.text = q.answers[2]
    btnD.text = q.answers[3]

    btnA.visibility = View.VISIBLE
    btnB.visibility = View.VISIBLE
    btnC.visibility = View.VISIBLE
    btnD.visibility = View.VISIBLE
    btnBack.visibility = View.GONE
}


    private fun answerSelected(selectedIndex: Int) {
        val correct = questions[currentQuestionIndex].correctIndex

        if (selectedIndex == correct) {
            scores[currentPlayer]++
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun showPlayerResult() {
        timer?.cancel()

        val result = "${scores[currentPlayer]}/$questionsPerPlayer"

        txtQuestion.text = "Zawodnik ${currentPlayer + 1}\n\nWynik: $result"
        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text = "Następny zawodnik"

        btnBack.setOnClickListener {
            currentPlayer++
            currentQuestionIndex = 0

            if (currentPlayer >= playersCount) {
                endQuiz()
            } else {
                showQuestion()
            }
        }
    }

    private fun endQuiz() {
        timer?.cancel()

        val resultText = StringBuilder("Koniec quizu\n\n")
        for (i in 0 until playersCount) {
            resultText.append("Zawodnik ${i + 1}: ${scores[i]}/${questionsPerPlayer}\n")
        }

        txtQuestion.text = resultText.toString()
        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text = "Zakończ"
        btnBack.setOnClickListener { finish() }
    }
}
