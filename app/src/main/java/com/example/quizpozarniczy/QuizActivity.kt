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
import com.example.quizpozarniczy.model.PlayerResult
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

        timePerPlayerSeconds =
            intent.getIntExtra("TIME_SECONDS", 60)

        scores = IntArray(playersCount)

        // 🔥 PYTANIA LOKALNE
        val localQuestions = LocalQuestionsRepository.questions
            .shuffled()
            .take(localQuestionsLimit)
            .map {
                Question(
                    text = it.fullQuestion(),
                    answers = it.answers,
                    correctIndex = it.correctIndex
                )
            }

        // 🔥 PYTANIA OGÓLNE
        val normalQuestionsCount = questionsLimit - localQuestions.size

        val normalQuestions = QuizRepository.getQuestions()
            .shuffled()
            .take(normalQuestionsCount)

        // 🔥 POŁĄCZENIE + LOSOWA KOLEJNOŚĆ
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

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000) {

            override fun onTick(ms: Long) {
                val totalSeconds = ms / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60

                txtTimer.text =
                    String.format("%02d:%02d", minutes, seconds)

                if (totalSeconds <= 10) {
                    txtTimer.setTextColor(
                        getColor(android.R.color.holo_red_dark)
                    )
                } else {
                    txtTimer.setTextColor(
                        getColor(android.R.color.black)
                    )
                }

                if (totalSeconds in 1..3) {
                    toneGenerator.startTone(
                        ToneGenerator.TONE_PROP_BEEP,
                        150
                    )
                }
            }

            override fun onFinish() {
                txtTimer.text = "00:00"
                txtTimer.setTextColor(
                    getColor(android.R.color.holo_red_dark)
                )

                toneGenerator.startTone(
                    ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,
                    2000
                )

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

        txtQuestion.text =
            "Zawodnik ${currentPlayer + 1}\n\n${q.text}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]

        resetButtons()
        setAnswersEnabled(true)

        btnShowCorrect.visibility = View.GONE
        btnBack.visibility = View.GONE

        txtQuestionCounter.text =
            "${currentQuestionIndex + 1} / ${questions.size}"
    }

    private fun answerSelected(index: Int) {
        val q = questions[currentQuestionIndex]

        if (index == q.correctIndex) {
            scores[currentPlayer]++
        } else {
            wrongAnswersCurrentPlayer.add(
                WrongAnswer(
                    question = q.text,
                    answers = q.answers,
                    chosenIndex = index,
                    correctIndex = q.correctIndex
                )
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
            if (wrongAnswersCurrentPlayer.isNotEmpty())
                View.VISIBLE else View.GONE

        btnBack.visibility = View.VISIBLE
        btnBack.text =
            if (currentPlayer + 1 < playersCount)
                "Następny zawodnik"
            else
                "Zobacz wyniki"

        btnBack.setOnClickListener {
            btnShowCorrect.visibility = View.GONE
            currentPlayer++
            currentQuestionIndex = 0
            wrongAnswerIndex = 0
            resultSavedForPlayer = false

            if (currentPlayer < playersCount) {
                showQuestion()
            } else {
                showFinalResults()
            }
        }
    }

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
                w.correctIndex -> {
                    btn.setBackgroundColor(
                        getColor(R.color.answer_correct)
                    )
                }
                w.chosenIndex -> {
                    btn.setBackgroundColor(
                        getColor(R.color.answer_wrong)
                    )
                }
                else -> {
                    btn.setBackgroundColor(
                        getColor(android.R.color.darker_gray)
                    )
                }
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
        btnShowCorrect.visibility = View.GONE
        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        val sb = StringBuilder("Koniec quizu\n\n")
        playerResults.forEach {
            sb.append(
                "Zawodnik ${it.playerNumber}: ${it.score}/${it.total}\n"
            )
        }

        txtQuestion.text = sb.toString()
        txtTimer.text = ""

        btnBack.text = "Powrót do panelu sędziego"
        btnBack.visibility = View.VISIBLE
        btnBack.setOnClickListener { finish() }
    }

    private fun resetButtons() {
        val def = android.R.drawable.btn_default

        listOf(btnA, btnB, btnC).forEach { btn ->
            btn.setBackgroundResource(def)
            btn.setTextColor(getColor(android.R.color.black))
            btn.visibility = View.VISIBLE
            btn.isEnabled = true
        }
    }

    private fun setAnswersEnabled(enabled: Boolean) {
        btnA.isEnabled = enabled
        btnB.isEnabled = enabled
        btnC.isEnabled = enabled
    }
}
