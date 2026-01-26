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
    private lateinit var btnNext: Button

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

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnNext = findViewById(R.id.btnBack)

        playersCount = intent.getIntExtra("PLAYERS", 1)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)
        val questionsLimit = intent.getIntExtra("QUESTIONS", 10)

        scores = IntArray(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        showQuestion()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    // ================= TIMER =================
    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val sec = ms / 1000
                val min = sec / 60
                val s = sec % 60

                txtTimer.text = String.format("%02d:%02d", min, s)

                if (sec <= 10) {
                    txtTimer.setTextColor(getColor(android.R.color.holo_red_dark))
                } else {
                    txtTimer.setTextColor(getColor(android.R.color.black))
                }

                if (sec in 0..2) {
                    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                }
            }

            override fun onFinish() {
                tone.startTone(ToneGenerator.TONE_DTMF_0, 800)
                setAnswersEnabled(false)
                showPlayerResult()
            }
        }.start()
    }

    // ================= PYTANIA =================
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

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
        btnNext.visibility = View.GONE

        setAnswersEnabled(true)
    }

    private fun answerSelected(index: Int) {
        if (!btnA.isEnabled) return

        if (index == questions[currentQuestionIndex].correctIndex) {
            scores[currentPlayer]++
        }

        currentQuestionIndex++
        showQuestion()
    }

    // ================= WYNIK ZAWODNIKA =================
    private fun showPlayerResult() {
        timer?.cancel()
        timer = null

        txtQuestion.text =
            "Zawodnik ${currentPlayer + 1}\n\nWynik: ${scores[currentPlayer]}/${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        btnNext.visibility = View.VISIBLE
        btnNext.text =
            if (currentPlayer + 1 < playersCount) "Następny zawodnik" else "Zobacz wyniki końcowe"

        btnNext.setOnClickListener {
            currentPlayer++
            currentQuestionIndex = 0

            if (currentPlayer < playersCount) {
                showQuestion()
            } else {
                showFinalResults()
            }
        }
    }

    // ================= WYNIKI KOŃCOWE =================
    private fun showFinalResults() {
        val sb = StringBuilder("Wyniki końcowe\n\n")

        for (i in scores.indices) {
            sb.append("Zawodnik ${i + 1}: ${scores[i]}/${questions.size}\n")
        }

        txtQuestion.text = sb.toString()
        txtTimer.text = ""

        btnNext.text = "Powrót do panelu sędziego"
        btnNext.setOnClickListener { finish() }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
