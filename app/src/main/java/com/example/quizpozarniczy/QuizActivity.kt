package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.PlayerResult
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.util.QuizRepository
import com.example.quizpozarniczy.util.ResultStore

class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var index = 0
    private var score = 0

    override fun onBackPressed() {
        // blokada cofania
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questions = QuizRepository.load(this)
        showQuestion()
    }

    private fun showQuestion() {
        val q = questions[index]

        findViewById<TextView>(R.id.tvQuestion).text = q.question

        val buttons = listOf(
            findViewById<Button>(R.id.b1),
            findViewById<Button>(R.id.b2),
            findViewById<Button>(R.id.b3),
            findViewById<Button>(R.id.b4)
        )

        buttons.forEachIndexed { i, b ->
            b.text = q.answers[i]
            b.setOnClickListener { answer(i) }
        }
    }

    private fun answer(selected: Int) {
        if (selected == questions[index].correctIndex) {
            score++
        }

        index++

        if (index < questions.size) {
            showQuestion()
        } else {
            val playerIndex = intent.getIntExtra("PLAYER_INDEX", 1)
            val players = intent.getIntExtra("PLAYERS", 1)
            val time = intent.getIntExtra("TIME", 300)

            ResultStore.results.add(
                PlayerResult(playerIndex, score)
            )

            if (playerIndex < players) {
                val i = Intent(this, QuizActivity::class.java)
                i.putExtra("PLAYER_INDEX", playerIndex + 1)
                i.putExtra("PLAYERS", players)
                i.putExtra("TIME", time)
                startActivity(i)
            } else {
                startActivity(Intent(this, ResultActivity::class.java))
            }
            finish()
        }
    }
}
