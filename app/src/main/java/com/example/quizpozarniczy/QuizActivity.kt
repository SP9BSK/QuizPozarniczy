package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
    private lateinit var txtPlayerName: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button

    private val currentPlayer: Int
        get() = QuizSession.currentPlayer - 1

    private lateinit var currentQuestions: List<Question>
    private var answeredQuestions = 0
    private val wrongAnswersCurrentPlayer = mutableListOf<WrongAnswer>()

    private var timePerPlayerSeconds = 60
    private var timeLeftSeconds = 0
    private var timer: CountDownTimer? = null

    companion object {
        private const val MAX_QUESTIONS = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        txtQuestionCounter = findViewById(R.id.txtQuestionCounter)
        txtPlayerName = findViewById(R.id.txtPlayerName)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)

        val questionsLimit =
            min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)

        val localLimit =
            intent.getIntExtra("LOCAL_QUESTIONS", 0)

        timePerPlayerSeconds =
            intent.getIntExtra("TIME_SECONDS", 60)

        // 🔥 LOSOWANIE TYLKO RAZ (dla całego turnieju)
        if (QuizSession.questions.isEmpty()) {

            val localQuestions =
                LocalQuestionsRepository
                    .toQuizQuestions(MAX_QUESTIONS)
                    .shuffled()
                    .take(localLimit)

            val normalQuestions =
                QuizRepository
                    .getQuestions()
                    .shuffled()
                    .take(questionsLimit - localLimit)

            QuizSession.questions =
                (localQuestions + normalQuestions)
                    .shuffled()
                    .toMutableList()
        }

        currentQuestions = QuizSession.questions

        answeredQuestions = 0
        timeLeftSeconds = timePerPlayerSeconds

        showPlayerName()
        startTimer()
        showQuestion()
    }

    private fun showPlayerName() {
        if (currentPlayer !in QuizSession.playerNames.indices) {
            finish()
            return
        }

        txtPlayerName.text =
            "Zawodnik ${currentPlayer + 1}: ${QuizSession.playerNames[currentPlayer]}"
    }

    private fun showQuestion() {
        if (answeredQuestions >= currentQuestions.size) {
            finishPlayer()
            return
        }

        val q = currentQuestions[answeredQuestions]

        txtQuestion.text = q.text
        txtQuestionCounter.text =
            "Pytanie ${answeredQuestions + 1} / ${currentQuestions.size}"

        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]

        btnA.setOnClickListener { answer(0) }
        btnB.setOnClickListener { answer(1) }
        btnC.setOnClickListener { answer(2) }
    }

    private fun answer(index: Int) {
        val q = currentQuestions[answeredQuestions]

        if (index != q.correctIndex) {
            wrongAnswersCurrentPlayer.add(
                WrongAnswer(
                    question = q.text,
                    answers = q.answers,
                    chosenIndex = index,
                    correctIndex = q.correctIndex
                )
            )
        }

        answeredQuestions++
        showQuestion()
    }

    private fun finishPlayer() {
        timer?.cancel()

        val correctAnswers =
            currentQuestions.size - wrongAnswersCurrentPlayer.size

        QuizSession.results.add(
            PlayerResult(
                playerNumber = currentPlayer + 1,
                playerName = QuizSession.playerNames[currentPlayer],
                score = correctAnswers,
                total = currentQuestions.size,
                timeSeconds = timePerPlayerSeconds - timeLeftSeconds,
                wrongAnswers = wrongAnswersCurrentPlayer.toList()
            )
        )

        wrongAnswersCurrentPlayer.clear()

        QuizSession.currentPlayer++

        val i = Intent(this, PlayerResultActivity::class.java)
        i.putExtra("PLAYER_INDEX", QuizSession.results.size - 1)
        startActivity(i)

        finish()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLeftSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftSeconds = (millisUntilFinished / 1000).toInt()
                txtTimer.text = formatTime(timeLeftSeconds)
            }

            override fun onFinish() {
                finishPlayer()
            }
        }.start()
    }

    private fun formatTime(seconds: Int): String =
        String.format("%02d:%02d", seconds / 60, seconds % 60)
}
