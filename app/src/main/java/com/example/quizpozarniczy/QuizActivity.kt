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

        // ===================== Ustawienia =====================
        playersCount = min(intent.getIntExtra("PLAYERS", 1), MAX_PLAYERS)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        // Dodaj brakujące domyślne nazwy graczy, nie nadpisując istniejących
        while (QuizSession.playerNames.size < playersCount) {
            QuizSession.playerNames.add("Zawodnik ${QuizSession.playerNames.size + 1}")
        }
        if (QuizSession.playerNames.size > playersCount) {
            QuizSession.playerNames.subList(playersCount, QuizSession.playerNames.size).clear()
        }

        scores = IntArray(playersCount)

        // ===================== Przygotowanie pytań =====================
        val questionsLimit = min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)
        val localQuestionsLimit = intent.getIntExtra("LOCAL_QUESTIONS", 1).coerceIn(1, 3)
        val localQuestions = LocalQuestionsRepository.toQuizQuestions(localQuestionsLimit)
        val normalQuestions = QuizRepository.getQuestions()
            .shuffled()
            .take(questionsLimit - localQuestions.size)
        questions = (localQuestions + normalQuestions).shuffled()

        // ===================== Przypisanie listenerów =====================
        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnShowCorrect.setOnClickListener {
            wrongAnswerIndex = 0
            showWrongAnswer()
        }

        // Rozpocznij quiz od pierwszego pytania
        showQuestion()
    }

    // ===================== TIMER =====================
    private fun startTimer() {
        timer?.cancel()
        timeLeftSeconds = timePerPlayerSeconds

        timer = object : CountDownTimer(timePerPlayerSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftSeconds = (millisUntilFinished / 1000).toInt()
                txtTimer.text = formatTime(timeLeftSeconds)
            }

            override fun onFinish() {
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300)
                showPlayerResult()
            }
        }.start()
    }

    // ===================== WYŚWIETLANIE PYTAŃ =====================
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

        txtQuestion.text = "${QuizSession.playerNames[currentPlayer]}\n\n${q.text}"
        txtQuestionCounter.text = "${currentQuestionIndex + 1} / ${questions.size}"

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
                WrongAnswer(q.text, q.answers, index, q.correctIndex)
            )
        }

        currentQuestionIndex++
        showQuestion()
    }

    // ===================== WYNIK GRACZA =====================
    private fun showPlayerResult() {
        timer?.cancel()

        if (!resultSavedForPlayer) {
            val timeUsed = timePerPlayerSeconds - timeLeftSeconds

            QuizSession.results.add(
                PlayerResult(
                    playerNumber = currentPlayer + 1,
                    playerName = QuizSession.playerNames[currentPlayer],
                    score = scores[currentPlayer],
                    total = questions.size,
                    timeSeconds = timeUsed,
                    wrongAnswers = wrongAnswersCurrentPlayer.toList()
                )
            )
            resultSavedForPlayer = true
        }

        txtQuestion.text =
            "${QuizSession.playerNames[currentPlayer]}\n\nWynik: ${scores[currentPlayer]}/${questions.size}"
        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE

        btnShowCorrect.visibil
