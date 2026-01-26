package com.example.quizpozarniczy

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
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

    private lateinit var questions: List<Question>
    private lateinit var scores: IntArray

    private var player = 0
    private var qIndex = 0
    private var playersCount = 1
    private var timeSeconds = 60
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
        timeSeconds = intent.getIntExtra("TIME", 60)
        val limit = intent.getIntExtra("QUESTIONS", 30)

        scores = IntArray(playersCount)

        val all = QuizRepository.getQuestions()
        questions = all.shuffled().take(min(limit, all.size))

        startTimer()
        showQuestion()

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val s = ms / 1000
                txtTimer.text = "${s / 60}:${String.format("%02d", s % 60)}"
                if (s in 0..2) tone.startTone(ToneGenerator.TONE_PROP_BEEP)
            }
            override fun onFinish() {
                showPlayerResult()
            }
        }.start()
    }

    private fun showQuestion() {
        val q = questions[qIndex]
        txtQuestion.text = "Zawodnik ${player + 1}\n\n${q.text}"
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
    }

    private fun answer(i: Int) {
        if (i == questions[qIndex].correctIndex) scores[player]++
        qIndex++
        if (qIndex < questions.size) showQuestion() else showPlayerResult()
    }

    private fun showPlayerResult() {
        timer?.cancel()
        txtQuestion.text = "Zawodnik ${player + 1}\nWynik: ${scores[player]}/${questions.size}"
        btnNext.text = if (player + 1 < playersCount) "Następny" else "Wyniki końcowe"
        btnNext.setOnClickListener {
            player++
            qIndex = 0
            if (player < playersCount) {
                startTimer()
                showQuestion()
            } else finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        tone.release()
    }
}
