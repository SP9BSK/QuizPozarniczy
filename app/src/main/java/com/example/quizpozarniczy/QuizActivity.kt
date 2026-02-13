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
import com.example.quizpozarniczy.model.WrongAnswer
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnShowAnswers: Button
    private lateinit var btnNext: Button

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var playersCount = 1
    private var score = 0
    private var timePerPlayerSeconds = 60

    private var timer: CountDownTimer? = null
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnShowAnswers = findViewById(R.id.btnShowAnswers)
        btnNext = findViewById(R.id.btnNext)

        playersCount = intent.getIntExtra("PLAYERS", 1)
        val questionsLimit = intent.getIntExtra("QUESTIONS", 5)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        QuizSession.currentPlayer = 1
        QuizSession.results.clear()
        QuizSession.totalPlayers = playersCount
        QuizSession.ensurePlayers(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        btnShowAnswers.setOnClickListener {
            startActivity(Intent(this, PlayerAnswersActivity::class.java))
        }

        btnNext.setOnClickListener {
            goToNextPlayerOrFinish()
        }

        showQuestion()
    }

    // ================= PYTANIA =================

    private fun showQuestion() {

        if (currentQuestionIndex == 0) {
            startTimer()
            score = 0
            wrongAnswersCurrentPlayer.clear()
        }

        if (currentQuestionIndex >= questions.size) {
            finishPlayer()
            return
        }

        val q = questions[currentQuestionIndex]
        val playerName = QuizSession.playerNames
            .getOrNull(QuizSession.currentPlayer - 1)
            ?: "Zawodnik ${QuizSession.currentPlayer}"

        txtQuestion.text =
            "$playerName\nPytanie ${currentQuestionIndex + 1}/${questions.size}\n\n${q.text}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
        btnShowAnswers.visibility = View.GONE
        btnNext.visibility = View.GONE
    }

    private fun answerSelected(index: Int) {

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

    // ================= TIMER =================

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val seconds = ms / 1000
                val minutesPart = seconds / 60
                val secondsPart = seconds % 60
                txtTimer.text = String.format("%02d:%02d", minutesPart, secondsPart)
            }

            override fun onFinish() {
                finishPlayer()
            }
        }.start()
    }

    // ================= KONIEC ZAWODNIKA =================

    private fun finishPlayer() {

        timer?.cancel()

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
                wrongAnswers = wrongAnswersCurrentPlayer.toList()
            )
        )

        txtQuestion.text =
            "$playerName\n\nWynik: $score/${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        btnShowAnswers.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE

        btnNext.text =
            if (QuizSession.currentPlayer < playersCount)
                "Następny zawodnik"
            else
                "Pokaż wyniki"
    }

    private fun goToNextPlayerOrFinish() {

        if (QuizSession.currentPlayer < playersCount) {
            QuizSession.currentPlayer++
            currentQuestionIndex = 0
            showQuestion()
        } else {
            startActivity(Intent(this, ResultActivity::class.java))
            finish()
        }
    }
}
