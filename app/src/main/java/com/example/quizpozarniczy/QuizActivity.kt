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

    private var wrongAnswerIndex = 0

    private lateinit var scores: IntArray
    private val playerResults = mutableListOf<PlayerResult>()
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    private var timePerPlayerSeconds = 60
    private var timer: CountDownTimer? = null

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
        private const val MAX_TIME_SECONDS = 30 * 60
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

        btnShowCorrect.visibility = View.GONE

        btnShowCorrect.setOnClickListener {
            wrongAnswerIndex = 0
            showWrongAnswers()
        }

        var questionsLimit = intent.getIntExtra("QUESTIONS", 5)
        if (questionsLimit > MAX_QUESTIONS) questionsLimit = MAX_QUESTIONS

        var timeFromIntent = intent.getIntExtra("TIME_SECONDS", 60)
        if (timeFromIntent > MAX_TIME_SECONDS) timeFromIntent = MAX_TIME_SECONDS
        timePerPlayerSeconds = timeFromIntent

        var playersFromIntent = intent.getIntExtra("PLAYERS", 1)
        if (playersFromIntent > MAX_PLAYERS) playersFromIntent = MAX_PLAYERS
        playersCount = playersFromIntent

        scores = IntArray(playersCount)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))

        showQuestion()

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val totalSeconds = ms / 1000
                txtTimer.text = String.format(
                    "%02d:%02d",
                    totalSeconds / 60,
                    totalSeconds % 60
                )
            }

            override fun onFinish() {
                setAnswersEnabled(false)
                showPlayerResult()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentQuestionIndex == 0) {
            wrongAnswersCurrentPlayer.clear()
            btnShowCorrect.visibility = View.GONE
            resetAnswerColors()
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

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
        btnBack.visibility = View.GONE

        setAnswersEnabled(true)
    }

    private fun answerSelected(index: Int) {
        if (!btnA.isEnabled) return

        val q = questions[currentQuestionIndex]

        if (index == q.correctIndex) {
            scores[currentPlayer]++
        } else {
            wrongAnswersCurrentPlayer.add(
                WrongAnswer(
                    question = q.text,
                    chosenAnswer = q.answers[index],
                    correctAnswer = q.answers[q.correctIndex]
                )
            )
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun showPlayerResult() {
        timer?.cancel()

        playerResults.add(
            PlayerResult(
                playerNumber = currentPlayer + 1,
                score = scores[currentPlayer],
                total = questions.size,
                wrongAnswers = wrongAnswersCurrentPlayer.toList()
            )
        )

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

            if (currentPlayer < playersCount) {
                showQuestion()
            } else {
                showFinalResults()
            }
        }
    }

    private fun showWrongAnswers() {
        if (wrongAnswerIndex >= wrongAnswersCurrentPlayer.size) {
            btnShowCorrect.visibility = View.GONE
            showPlayerResult()
            return
        }

        val w = wrongAnswersCurrentPlayer[wrongAnswerIndex]

        txtQuestion.text =
            "Pytanie ${wrongAnswerIndex + 1}/${wrongAnswersCurrentPlayer.size}\n\n${w.question}"

        val buttons = listOf(btnA, btnB, btnC)

        for (btn in buttons) {
            btn.visibility = View.VISIBLE
            btn.isEnabled = false

            when (btn.text.toString()) {
                w.correctAnswer ->
                    btn.setBackgroundColor(getColor(R.color.answer_correct))
                w.chosenAnswer ->
                    btn.setBackgroundColor(getColor(R.color.answer_wrong))
                else ->
                    btn.setBackgroundColor(getColor(android.R.color.darker_gray))
            }
        }

        btnBack.visibility = View.VISIBLE
        btnBack.text = "Dalej"

        btnBack.setOnClickListener {
            resetAnswerColors()
            wrongAnswerIndex++
            showWrongAnswers()
        }
    }

    private fun resetAnswerColors() {
        btnA.setBackgroundColor(getColor(android.R.color.holo_blue_light))
        btnB.setBackgroundColor(getColor(android.R.color.holo_blue_light))
        btnC.setBackgroundColor(getColor(android.R.color.holo_blue_light))
    }

    private fun showFinalResults() {
        val result = StringBuilder("Koniec quizu\n\n")
        for (p in playerResults) {
            result.append("Zawodnik ${p.playerNumber}: ${p.score}/${p.total}\n")
        }

        txtQuestion.text = result.toString()
        txtTimer.text = ""

        btnBack.text = "Powrót do panelu sędziego"
        btnBack.setOnClickListener { finish() }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
