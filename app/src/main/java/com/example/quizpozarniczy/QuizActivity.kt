package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private var score = 0
    private var index = 0
    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val txtTitle = findViewById<TextView>(R.id.txtTitle)
        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val btnYes = findViewById<Button>(R.id.btnYes)
        val btnNo = findViewById<Button>(R.id.btnNo)

        val limit = intent.getIntExtra("QUESTIONS", 10)
        questions = QuizRepository.getQuestions().take(limit)

        txtTitle.text = "Zawodnik ${QuizSession.currentPlayer}"

        fun showQuestion() {
            if (index >= questions.size) {
                QuizSession.results.add(
                    PlayerResult(QuizSession.currentPlayer, score)
                )

                if (QuizSession.currentPlayer < QuizSession.totalPlayers) {
                    QuizSession.currentPlayer++
                    startActivity(Intent(this, QuizActivity::class.java))
                } else {
                    startActivity(Intent(this, RankingActivity::class.java))
                }
                finish()
                return
            }

            txtQuestion.text = questions[index].text
        }

        btnYes.setOnClickListener {
            if (questions[index].correct) score++
            index++
            showQuestion()
        }

        btnNo.setOnClickListener {
            if (!questions[index].correct) score++
            index++
            showQuestion()
        }

        showQuestion()
    }
}
