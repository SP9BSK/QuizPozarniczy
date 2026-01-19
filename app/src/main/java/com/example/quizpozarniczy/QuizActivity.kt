package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Player

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var playerText: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    private val questions = listOf(
        "Czy woda przewodzi prąd?",
        "Czy gaśnica pianowa nadaje się do gaszenia urządzeń elektrycznych?",
        "Czy dym jest bardziej niebezpieczny niż ogień?",
        "Czy hełm strażacki chroni przed temperaturą?",
        "Czy można gasić olej wodą?"
    )

    private var maxQuestions = 0
    private var currentQuestionIndex = 0

    private var players: List<Player> = emptyList()
    private var currentPlayerIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionText)
        playerText = findViewById(R.id.playerText)
        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)

        // dane z panelu sędziego
        val playerCount = intent.getIntExtra("PLAYER_COUNT", 1)
        maxQuestions = intent.getIntExtra("QUESTION_COUNT", 1)

        players = (1..playerCount).map { Player(it) }

        showQuestion()

        yesButton.setOnClickListener {
            players[currentPlayerIndex].points++
            nextTurn()
        }

        noButton.setOnClickListener {
            nextTurn()
        }
    }

    private fun showQuestion() {
        questionText.text = questions[currentQuestionIndex]
        playerText.text = "Zawodnik ${players[currentPlayerIndex].id}"
    }

    private fun nextTurn() {
        currentPlayerIndex++

        if (currentPlayerIndex >= players.size) {
            currentPlayerIndex = 0
            currentQuestionIndex++
        }

        if (currentQuestionIndex >= maxQuestions) {
            endQuiz()
        } else {
            showQuestion()
        }
    }

    private fun endQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(
            "RESULTS",
            players.joinToString("\n") {
                "Zawodnik ${it.id}: ${it.points} pkt"
            }
        )
        startActivity(intent)
        finish()
    }
}
