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

        // Ile pytań i zawodników
        val questionsLimit = min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)
        val localQuestionsLimit = intent.getIntExtra("LOCAL_QUESTIONS", 1).coerceIn(1, 3)
        timePerPlayerSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        // Używamy nazw z QuizSession
        playersCount = QuizSession.playerNames.size
        scores = IntArray(playersCount)

        // Pytania lokalne i główne
        val localQuestions = LocalQuestionsRepository.toQuizQuestions(localQuestionsLimit)
        val normalQuestions = QuizRepository.getQuestions()

        questions = localQuestions + normalQuestions

        // Start quizu dla pierwszego gracza
        showQuestion()
    }

    private fun showQuestion() {
        // Placeholder – implementacja pokazywania pytań
        txtQuestion.text = "${QuizSession.playerNames[currentPlayer]}\n\nWynik: ${scores[currentPlayer]}/${questions.size}"
    }

    private fun formatTime(seconds: Int): String = String.format("%02d:%02d", seconds / 60, seconds % 60)
}
