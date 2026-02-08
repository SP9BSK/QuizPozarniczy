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
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.PlayerResult
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.model.WrongAnswer
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var txtQuestionCounter: TextView
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

    private var wrongAnswerIndex = 0
    private var resultSavedForPlayer = false

    private var timePerPlayerSeconds = 60
    private var timeLeftSeconds = 0
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
        txtQuestionCounter = findViewById(R.id.txtQuestionCounter)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnBack = findViewById(R.id.btnBack)
        btnShowCorrect = findViewById(R.id.btnShowCorrect)

        val questionsLimit =
            min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)

        val localQuestionsLimit =
            intent.getIntExtra("LOCAL_QUESTIONS", 1).coerceIn(1, 3)

        playersCount =
            min(intent.getIntExtra("PLAYERS", 1), MAX_PLAYERS)

        scores = IntArray(playersCount)

        val localQuestions = LocalQuestionsRepository
            .toQuizQuestions(localQuestionsLimit)

        val normalQuestionsCount = questionsLimit - localQuestions.size
        val normalQuestions = QuizRepository.getQuestions()
            .shuffled()
            .take(normalQuestionsCount)

        questions = (localQuestions + normalQuestions).shuffled()

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        btnShowCorrect.setOnClickListener {
            wrongAnswerIndex = 0
            showWrongAnswer()
        }

        showQuestion()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    // ================= WYNIKI KOŃCOWE =================

    private fun showFinalResults() {
        btnShowCorrect.visibility = View.GONE
        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        val sb = StringBuilder("Koniec quizu\n\n")

        val sorted = playerResults.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        // Wyświetlanie wyników w formie "numer miejsca. Zawodnik X: punkty | czas"
        for ((position, result) in sorted.withIndex()) {
            sb.append(
                "${position + 1}. Zawodnik ${result.playerNumber}: " +
                "${result.score}/${result.total} | " +
                "Czas: ${formatTime(result.timeSeconds)}\n"
            )
        }

        txtQuestion.text = sb.toString()
        txtTimer.text = ""

        btnBack.text = "Powrót do panelu sędziego"
        btnBack.visibility = View.VISIBLE
        btnBack.setOnClickListener { finish() }
    }

    // ================= BŁĘDY =================

    private fun showWrongAnswer() {
        btnShowCorrect.visibility = View.GONE

        if (wrongAnswerIndex >= wrongAnswersCurrentPlayer.size) {
            showPlayerResult()
            return
        }

        val w = wrongAnswersCurrentPlayer[wrongAnswerIndex]

        txtQuestion.text =
            "Pytanie ${wrongAnswerIndex + 1}/${wrongAnswersCurrentPlayer.size}\n\n${w.question}"

        val buttons = listOf(btnA, btnB, btnC)

        for ((i, btn) in buttons.withIndex()) {
            btn.visibility = View.VISIBLE
            btn.isEnabled = false
            btn.text = w.answers[i]

            when (i) {
                w.correctIndex -> btn.setBackgroundColor(getColor(R.color.answer_correct))
                w.chosenIndex -> btn.setBackgroundColor(getColor(R.color.answer_wrong))
                else -> btn.setBackgroundColor(getColor(android.R.color.darker_gray))
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

    // ================= UTIL =================

    private fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }

    private fun resetButtons() {
        val def = android.R.drawable.btn_default

        listOf(btnA, btnB, btnC).forEach {
            it.setBackgroundResource(def)
            it.setTextColor(getColor(android.R.color.black))
            it.visibility = View.VISIBLE
            it.isEnabled = true
        }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
