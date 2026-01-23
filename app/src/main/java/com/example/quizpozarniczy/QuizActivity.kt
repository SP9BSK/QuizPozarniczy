package com.example.quizpozarniczy

import android.media.AudioManager
import android.media.ToneGenerator
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
    private var playersCount = 1
    private lateinit var scores: IntArray

    private var timePerPlayerSeconds = 60
    private var timer: CountDownTimer? = null

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

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

        val questionsLimit = intent.getIntExtra("QUESTIONS", 5)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)
        playersCount = min(intent.getIntExtra("PLAYERS", 1), 10)

        scores = IntArray(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled().take(questionsLimit)

        showQuestion()

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnD.setOnClickListener { answerSelected(3) }

        btnBack.setOnClickListener { finish() }
    }

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val sec = ms / 1000
                txtTimer.text = "Czas: $sec s"

                if (ms <= 10_000) {
                    txtTimer.setTextColor(getColor(android.R.color.holo_red_dark))
                } else {
                    txtTimer.setTextColor(getColor(android.R.color.black))
                }

                if (sec in 1..3) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                }
            }

            override fun onFinish() {
                setAnswersEnabled(false)
                showPlayerResult()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentQuestionIndex == 0) {
            startTimer()
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

        setAnswersEnabled(true)
    }

    private fun answerSelected(index: Int) {
        if (index == questions[currentQuestionIndex].correctIndex) {
            scores[currentPlayer]++
        }
        currentQuestionIndex++
        showQuestion()
    }

    private fun showPlayerResult() {
        timer?.cancel()

        txtQuestion.text =
            "Zawodnik ${currentPlayer + 1}\n\nWynik: ${scores[currentPlayer]}/${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text = if (currentPlayer + 1 < playersCount)
            "Następny zawodnik"
        else
            "Zakończ"

        btnBack.setOnClickListener {
            currentPlayer++
            currentQuestionIndex = 0

            if (currentPlayer < playersCount) {
                showQuestion()
            } else {
               showFinalResults()

            }
        }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
        btnD.isEnabled = enabled
    }
    private fun showFinalResults() {
    timer?.cancel()

    val resultText = StringBuilder("Koniec quizu\n\n")

    for (i in 0 until playersCount) {
        resultText.append(
            "Zawodnik ${i + 1}: ${scores[i]}/${questions.size}\n"
        )
    }

    txtQuestion.text = resultText.toString()
    txtTimer.text = ""

    btnA.visibility = View.GONE
    btnB.visibility = View.GONE
    btnC.visibility = View.GONE
    btnD.visibility = View.GONE

    btnBack.visibility = View.VISIBLE
    btnBack.text = "Zamknij quiz"
    btnBack.setOnClickListener { finish() }
}

}

