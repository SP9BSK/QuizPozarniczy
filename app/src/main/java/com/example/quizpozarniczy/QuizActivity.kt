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

    private lateinit var questions: List<Question>

    private var currentQuestionIndex = 0
    private var score = 0
    private var timePerPlayerSeconds = 60
    private var playersCount = 1

    private var timeLeftMillis: Long = 0
    private var timer: CountDownTimer? = null

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

        playersCount = min(intent.getIntExtra("PLAYERS", 1), MAX_PLAYERS)
        val questionsLimit = min(intent.getIntExtra("QUESTIONS", 5), MAX_QUESTIONS)
        timePerPlayerSeconds = min(intent.getIntExtra("TIME_SECONDS", 60), MAX_TIME_SECONDS)

        // Reset tylko przy pierwszym zawodniku
        if (QuizSession.currentPlayer == 1 && QuizSession.results.isEmpty()) {
            QuizSession.totalPlayers = playersCount
            QuizSession.ensurePlayers(playersCount)

            val allQuestions = QuizRepository.getQuestions()
            QuizSession.questions =
                allQuestions
                    .shuffled()
                    .take(min(questionsLimit, allQuestions.size))
                    .toMutableList()
        }

        questions = QuizSession.questions

        btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }

        startTimer()
        showQuestion()
    }

    // ===== TIMER =====
   private fun startTimer() {
    timer?.cancel()

    val totalMillis = timePerPlayerSeconds * 1000L

    timer = object : CountDownTimer(totalMillis, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            timeLeftMillis = millisUntilFinished

            val totalSeconds = millisUntilFinished / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60

            updateTopBar(minutes, seconds)
        }

        override fun onFinish() {
            timeLeftMillis = 0
            finishPlayer()
        }

    }.start()
}

private fun updateTopBar(minutes: Long, seconds: Long) {
    txtTimer.text = String.format(
        "Czas: %02d:%02d  |  Pytanie %d/%d",
        minutes,
        seconds,
        currentQuestionIndex + 1,
        questions.size
    )
}
   
    private fun calculateElapsedTime(): Int {
        val totalMillis = timePerPlayerSeconds * 1000L
        val usedMillis = totalMillis - timeLeftMillis
        return (usedMillis / 1000).toInt()
    }

    // ===== PYTANIA =====
    private fun showQuestion() {
    if (currentQuestionIndex >= questions.size) {
        finishPlayer()
        return
    }

    val q = questions[currentQuestionIndex]

    // TYLKO TREŚĆ PYTANIA (bez licznika)
    txtQuestion.text = q.text

    btnA.text = q.answers[0]
    btnB.text = q.answers[1]
    btnC.text = q.answers[2]

   
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

    // ===== KONIEC ZAWODNIKA =====
    private fun finishPlayer() {
        timer?.cancel()

        val playerName = QuizSession.playerNames
            .getOrNull(QuizSession.currentPlayer - 1)
            ?: "Zawodnik ${QuizSession.currentPlayer}"

        if (QuizSession.results.size < QuizSession.currentPlayer) {
            QuizSession.results.add(
                PlayerResult(
                    playerNumber = QuizSession.currentPlayer,
                    playerName = playerName,
                    score = score,
                    total = questions.size,
                    timeSeconds = calculateElapsedTime(),
                    wrongAnswers = wrongAnswersCurrentPlayer.toList()
                )
            )
        }

        val intent = Intent(this, PlayerResultActivity::class.java)
        intent.putExtra("PLAYER_INDEX", QuizSession.currentPlayer - 1)
        startActivity(intent)
        finish()
    }
}
