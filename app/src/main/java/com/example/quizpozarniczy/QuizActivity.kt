package com.example.quizpozarniczy

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.PlayerResult
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
    private var score = 0
    private var timePerPlayerSeconds = 60
    private var playersCount = 1
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

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
        btnShowAnswers = findViewById(R.id.btnShowCorrect)
        btnNext = findViewById(R.id.btnBack)


        // ===== Dane wejściowe =====
        playersCount = min(intent.getIntExtra("PLAYERS", 1), MAX_PLAYERS)
        val questionsLimit = min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)
        timePerPlayerSeconds = min(intent.getIntExtra("TIME_SECONDS", 60), MAX_TIME_SECONDS)

        // Reset sesji tylko przy pierwszym wejściu
        if (QuizSession.results.isEmpty()) {
            QuizSession.currentPlayer = 1
            QuizSession.totalPlayers = playersCount
            QuizSession.ensurePlayers(playersCount)
        }

        // ===== Pytania (JEDEN zestaw dla wszystkich) =====
        if (questions.isEmpty()) {
            val allQuestions = QuizRepository.getQuestions()
            questions = allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))
        }

        // ===== Odpowiedzi =====
        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        btnShowAnswers.setOnClickListener {
            val intent = Intent(this, PlayerResultActivity::class.java)
            intent.putExtra("PLAYER_INDEX", QuizSession.currentPlayer - 1)
            startActivity(intent)
        }

        btnNext.setOnClickListener {
            goToNextPlayerOrFinish()
        }

        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            finishPlayer()
            return
        }

        val q = questions[currentQuestionIndex]
        txtQuestion.text = "Pytanie ${currentQuestionIndex + 1}/${questions.size}\n\n${q.text}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]

        btnA.visibility = View.VISIBLE
        btnB.visibility = View.VISIBLE
        btnC.visibility = View.VISIBLE
        txtTimer.visibility = View.VISIBLE

        btnShowAnswers.visibility = View.GONE
        btnNext.visibility = View.GONE
    }

    private fun answerSelected(index: Int) {
        val currentQ = questions[currentQuestionIndex]

        if (index != currentQ.correctIndex) {
            wrongAnswersCurrentPlayer.add(
                WrongAnswer(
                    question = currentQ.text,
                    answers = currentQ.answers,
                    chosenIndex = index,
                    correctIndex = currentQ.correctIndex
                )
            )
        } else {
            score++
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun finishPlayer() {

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

        wrongAnswersCurrentPlayer.clear()

        txtQuestion.text = "$playerName\n\nWynik: $score/${questions.size}"

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        txtTimer.visibility = View.GONE

        btnShowAnswers.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE

        btnShowAnswers.isEnabled =
            QuizSession.results.last().wrongAnswers.isNotEmpty()

        btnNext.text =
            if (QuizSession.currentPlayer < playersCount)
                "Następny zawodnik"
            else
                "Pokaż wyniki końcowe"
    }

    private fun goToNextPlayerOrFinish() {
        if (QuizSession.currentPlayer < playersCount) {
            QuizSession.currentPlayer++
            currentQuestionIndex = 0
            score = 0
            wrongAnswersCurrentPlayer.clear()
            showQuestion()
        } else {
            startActivity(Intent(this, ResultActivity::class.java))
            finish()
        }
    }
}
