package com.example.quizpozarniczy

import android.content.Intent
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

    private lateinit var questions: List<Question>

    private var currentQuestionIndex = 0
    private var score = 0
    private var timePerPlayerSeconds = 60
    private var playersCount = 1
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    private var timer: CountDownTimer? = null

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

        playersCount = min(intent.getIntExtra("PLAYERS", 1), MAX_PLAYERS)
        val questionsLimit = min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)
        timePerPlayerSeconds = min(intent.getIntExtra("TIME_SECONDS", 60), MAX_TIME_SECONDS)

        // ===== RESET TYLKO PRZY PIERWSZYM WEJŚCIU =====
        if (QuizSession.currentPlayer == 1 && QuizSession.results.isEmpty()) {
            QuizSession.totalPlayers = playersCount
            QuizSession.ensurePlayers(playersCount)

            val allQuestions = QuizRepository.getQuestions()
            QuizSession.questions =
                allQuestions.shuffled().take(min(questionsLimit, allQuestions.size))
        }

        questions = QuizSession.questions

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

        startTimer()
        showQuestion()
    }

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer((timePerPlayerSeconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                txtTimer.text = "Czas: $seconds s"
            }

            override fun onFinish() {
                finishPlayer()
            }
        }.start()
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

        txtQuestion.text = "$playerName\n\nWynik: $score/${questions.size}"

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        txtTimer.visibility = View.GONE

        btnShowAnswers.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE

        btnNext.text =
            if (QuizSession.currentPlayer < playersCount)
                "Następny zawodnik"
            else
                "Pokaż wyniki końcowe"
    }

    private fun goToNextPlayerOrFinish() {
        if (QuizSession.currentPlayer < playersCount) {
            QuizSession.currentPlayer++
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, ResultActivity::class.java))
            finish()
        }
    }
}
