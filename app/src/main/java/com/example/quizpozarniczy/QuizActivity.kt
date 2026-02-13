package com.example.quizpozarniczy

import android.content.Intent
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
import com.example.quizpozarniczy.model.WrongAnswer


class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnBack: Button

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var playersCount = 1
    private var score = 0

    private var timePerPlayerSeconds = 60
    private var timer: CountDownTimer? = null

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
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
        btnBack = findViewById(R.id.btnBack)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        QuizSession.currentPlayer = 1
        QuizSession.results.clear()

        var questionsLimit = intent.getIntExtra("QUESTIONS", 5)
        questionsLimit = min(questionsLimit, MAX_QUESTIONS)

        var timeFromIntent = intent.getIntExtra("TIME_SECONDS", 60)
        timeFromIntent = min(timeFromIntent, MAX_TIME_SECONDS)
        timePerPlayerSeconds = timeFromIntent

        var playersFromIntent = intent.getIntExtra("PLAYERS", 1)
        playersFromIntent = min(playersFromIntent, MAX_PLAYERS)
        playersCount = playersFromIntent

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled()
            .take(min(questionsLimit, allQuestions.size))

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
                val totalSeconds = ms / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60

                txtTimer.text = String.format("%02d:%02d", minutes, seconds)

                if (totalSeconds <= 10) {
                    txtTimer.setTextColor(getColor(android.R.color.holo_red_dark))
                } else {
                    txtTimer.setTextColor(getColor(android.R.color.black))
                }

                if (totalSeconds in 0..2) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                }
            }

            override fun onFinish() {
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 800)
                finishPlayer()
            }
        }.start()
    }

    // ================= PYTANIA =================
    private fun showQuestion() {

        if (currentQuestionIndex == 0) {
            startTimer()
            score = 0
        }

        if (currentQuestionIndex >= questions.size) {
            finishPlayer()
            return
        }

        val q = questions[currentQuestionIndex]
        val playerName = QuizSession.playerNames
            .getOrNull(QuizSession.currentPlayer - 1)
            ?: "Zawodnik ${QuizSession.currentPlayer}"

        txtQuestion.text = "$playerName\n\n${q.text}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
        btnBack.visibility = View.GONE

        setAnswersEnabled(true)
    }

    private fun answerSelected(index: Int) {
    if (!btnA.isEnabled) return

    val currentQ = questions[currentQuestionIndex]

    if (index == currentQ.correctIndex) {
        score++
    } else {
        wrongAnswersCurrentPlayer.add(
            WrongAnswer(
                question = currentQ.text,
                answers = currentQ.answers,
                chosenIndex = index,
                correctIndex = currentQ.correctIndex
            )
        )
    }

    currentQuestionIndex++
    showQuestion()
}


    // ================= ZAKOŃCZENIE ZAWODNIKA =================
    private fun finishPlayer() {

        timer?.cancel()
        timer = null

        val playerName = QuizSession.playerNames
            .getOrNull(QuizSession.currentPlayer - 1)
            ?: "Zawodnik ${QuizSession.currentPlayer}"

        QuizSession.results.add(
    PlayerResult(
        playerNumber = QuizSession.currentPlayer,
        playerName = playerName,
        score = score,
        total = questions.size,
        timeSeconds = timePerPlayerSeconds,
        wrongAnswers = wrongAnswersCurrentPlayer.toList()  // ✅ lista WrongAnswer
    )
)
wrongAnswersCurrentPlayer.clear()


        txtQuestion.text =
            "$playerName\n\nWynik: $score/${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text =
            if (QuizSession.currentPlayer < playersCount)
                "Następny zawodnik"
            else
                "Zobacz wyniki"

        btnBack.setOnClickListener {

            if (QuizSession.currentPlayer < playersCount) {
                QuizSession.currentPlayer++
                currentQuestionIndex = 0
                showQuestion()
            } else {
                showFinalResults()
            }
        }
    }

    // ================= WYNIKI KOŃCOWE =================
    private fun showFinalResults() {
        startActivity(Intent(this, ResultActivity::class.java))
        finish()
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
