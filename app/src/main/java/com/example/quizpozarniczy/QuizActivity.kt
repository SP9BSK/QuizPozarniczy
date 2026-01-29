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
import com.example.quizpozarniczy.model.PlayerResult
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.model.WrongAnswer
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnBack: Button
    private lateinit var btnShowCorrect: Button

    private var questions: List<Question> = emptyList()
    private var currentPlayer = 0
    private var currentQuestionIndex = 0
    private var playersCount = 1

    private lateinit var scores: IntArray
    private val playerResults = mutableListOf<PlayerResult>()
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    private var showingWrongAnswers = false
    private var wrongAnswerIndex = 0
    private var resultSavedForPlayer = false

    private var timePerPlayerSeconds = 60
    private var timer: CountDownTimer? = null

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnBack = findViewById(R.id.btnBack)
        btnShowCorrect = findViewById(R.id.btnShowCorrect)

        val questionsLimit = min(
            intent.getIntExtra("QUESTIONS", 5),
            MAX_QUESTIONS
        )

        playersCount = min(
            intent.getIntExtra("PLAYERS", 1),
            MAX_PLAYERS
        )

        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        scores = IntArray(playersCount)

        questions = QuizRepository.getQuestions()
            .shuffled()
            .take(questionsLimit)

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        btnShowCorrect.setOnClickListener {
            showingWrongAnswers = true
            wrongAnswerIndex = 0
            showWrongAnswer()
        }

        showQuestion()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val s = ms / 1000
                txtTimer.text = String.format("%02d:%02d", s / 60, s % 60)
            }

            override fun onFinish() {
                showPlayerResult()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentQuestionIndex == 0) {
            wrongAnswersCurrentPlayer.clear()
            resultSavedForPlayer = false
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

        resetButtons()
        setAnswersEnabled(true)

        btnBack.visibility = View.GONE
        btnShowCorrect.visibility = View.GONE
    }

    private fun answerSelected(index: Int) {
        val q = questions[currentQuestionIndex]

        if (index == q.correctIndex) {
            scores[currentPlayer]++
        } else {
            wrongAnswersCurrentPlayer.add(
                WrongAnswer(q.text, q.answers[index], q.answers[q.correctIndex])
            )
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun showPlayerResult() {
        timer?.cancel()

        if (!resultSavedForPlayer) {
            playerResults.add(
                PlayerResult(
                    currentPlayer + 1,
                    scores[currentPlayer],
                    questions.size,
                    wrongAnswersCurrentPlayer.toList()
                )
            )
            resultSavedForPlayer = true
        }

        txtQuestion.text =
            "Zawodnik ${currentPlayer + 1}\n\nWynik: ${scores[currentPlayer]}/${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        btnShowCorrect.visibility =
            if (wrongAnswersCurrentPlayer.isNotEmpty()) View.VISIBLE else View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text =
            if (currentPlayer + 1 < playersCount) "Następny zawodnik"
            else "Zobacz wyniki"

        btnBack.setOnClickListener {
            currentPlayer++
            currentQuestionIndex = 0
            resetButtons()

            if (currentPlayer < playersCount) showQuestion()
            else showFinalResults()
        }
    }

    private fun showWrongAnswer() {
        if (wrongAnswerIndex >= wrongAnswersCurrentPlayer.size) {
            btnShowCorrect.visibility = View.GONE
            return
        }

        val w = wrongAnswersCurrentPlayer[wrongAnswerIndex]

        txtQuestion.text =
            "Pytanie ${wrongAnswerIndex + 1}/${wrongAnswersCurrentPlayer.size}\n\n${w.question}"

        val buttons = listOf(btnA, btnB, btnC)

        for (b in buttons) {
            b.visibility = View.VISIBLE
            b.isEnabled = false

            when (b.text.toString()) {
                w.correctAnswer -> b.setBackgroundResource(R.color.answer_correct)
                w.chosenAnswer -> b.setBackgroundResource(R.color.answer_wrong)
                else -> b.setBackgroundResource(android.R.drawable.btn_default)
            }
        }

        btnBack.visibility = View.VISIBLE
        btnBack.text = "Dalej"
        btnBack.setOnClickListener {
            wrongAnswerIndex++
            resetButtons()
            showWrongAnswer()
        }
    }

    private fun showFinalResults() {
        val sb = StringBuilder("Koniec quizu\n\n")
        for (p in playerResults) {
            sb.append("Zawodnik ${p.playerNumber}: ${p.score}/${p.total}\n")
        }

        txtQuestion.text = sb.toString()
        btnBack.text = "Powrót"
        btnBack.setOnClickListener { finish() }
    }

    private fun resetButtons() {
        val def = android.R.drawable.btn_default
        btnA.setBackgroundResource(def)
        btnB.setBackgroundResource(def)
        btnC.setBackgroundResource(def)

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
