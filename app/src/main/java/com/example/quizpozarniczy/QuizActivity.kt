package com.example.quizpozarniczy

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 100
        private const val MAX_TIME_SECONDS = 30 * 60
    }

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

        // ===== WALIDACJA PANELU SĘDZIEGO =====

        var questionsLimit = intent.getIntExtra("QUESTIONS", 5)
        if (questionsLimit > MAX_QUESTIONS) {
            Toast.makeText(
                this,
                "Wpisano $questionsLimit pytań — użyto maksymalnie $MAX_QUESTIONS",
                Toast.LENGTH_LONG
            ).show()
            questionsLimit = MAX_QUESTIONS
        }

        var timeFromIntent = intent.getIntExtra("TIME_SECONDS", 60)
        if (timeFromIntent > MAX_TIME_SECONDS) {
            Toast.makeText(
                this,
                "Wpisano ${timeFromIntent / 60} min — użyto maks. 30 min",
                Toast.LENGTH_LONG
            ).show()
            timeFromIntent = MAX_TIME_SECONDS
        }
        timePerPlayerSeconds = timeFromIntent

        var playersFromIntent = intent.getIntExtra("PLAYERS", 1)
        if (playersFromIntent > MAX_PLAYERS) {
            Toast.makeText(
                this,
                "Wpisano $playersFromIntent zawodników — użyto maks. $MAX_PLAYERS",
                Toast.LENGTH_LONG
            ).show()
            playersFromIntent = MAX_PLAYERS
        }
        playersCount = playersFromIntent

        scores = IntArray(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))

        showQuestion()

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnD.setOnClickListener { answerSelected(3) }
    }

    // 🔒 EKRAN ZAWSZE WŁĄCZONY
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // ================= TIMER =================

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {

            override fun onTick(ms: Long) {
                val totalSeconds = ms / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60

                txtTimer.text = String.format("%02d:%02d", minutes, seconds)

                if (totalSeconds <= 10) {
                    txtTimer.setTextColor(getColor(android.R.color.holo_red_dark))
                } else {
                    txtTimer.setTextColor(getColor(android.R.color.black))
                }

                // 🔊 tylko 3 i 2
                if (totalSeconds == 3L || totalSeconds == 2L) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                }
            }

            override fun onFinish() {
                // 🔔 jeden długi dźwięk na 0
                toneGenerator.startTone(
                    ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,
                    1200
                )
                setAnswersEnabled(false)
                showPlayerResult()
            }
        }.start()
    }

    // ================= QUIZ =================

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

    // ================= WYNIKI =================

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
        btnBack.text =
            if (currentPlayer + 1 < playersCount) "Następny zawodnik" else "Zobacz wyniki"

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
        val result = StringBuilder("Koniec quizu\n\n")

        for (i in 0 until playersCount) {
            result.append("Zawodnik ${i + 1}: ${scores[i]}/${questions.size}\n")
        }

        txtQuestion.text = result.toString()
        txtTimer.text = ""

        btnBack.text = "Zamknij"
        btnBack.setOnClickListener { finish() }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
        btnD.isEnabled = enabled
    }
}
