package com.example.quizpozarniczy

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.quizpozarniczy.model.Question
import kotlin.math.min

class QuizActivity : BaseActivity() {

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
    private val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        playersCount = intent.getIntExtra("PLAYERS", 1)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)
        val limit = intent.getIntExtra("QUESTIONS", 10)

        scores = IntArray(playersCount)

        questions = QuizRepository
            .getQuestions()
            .shuffled()
            .take(min(limit, QuizRepository.getQuestions().size))

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
        btnD.setOnClickListener { answer(3) }

        showQuestion()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val s = ms / 1000
                txtTimer.text = String.format("%02d:%02d", s / 60, s % 60)

                if (s in 0..2) {
                    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                }
            }

            override fun onFinish() {
                tone.startTone(ToneGenerator.TONE_PROP_BEEP2, 800)
                showPlayerResult()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentQuestionIndex == 0) startTimer()

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

        btnBack.visibility = View.GONE
    }

    private fun answer(i: Int) {
        if (i == questions[currentQuestionIndex].correctIndex) {
            scores[currentPlayer]++
        }
        currentQuestionIndex++
        showQuestion()
    }

    private fun showPlayerResult() {
        timer?.cancel()

        txtQuestion.text =
            "Zawodnik ${currentPlayer + 1}\nWynik: ${scores[currentPlayer]}/${questions.size}"

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text =
            if (currentPlayer + 1 < playersCount) "Następny zawodnik" else "Wyniki końcowe"

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

    private fun showFinalResults() {
        val sb = StringBuilder("WYNIKI KOŃCOWE\n\n")
        scores.forEachIndexed { i, s ->
            sb.append("Zawodnik ${i + 1}: $s/${questions.size}\n")
        }
        txtQuestion.text = sb.toString()
        btnBack.text = "Zamknij"
        btnBack.setOnClickListener { finishAffinity() }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        tone.release()
    }
}
