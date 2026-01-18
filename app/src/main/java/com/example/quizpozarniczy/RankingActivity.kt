package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)

        val sorted = QuizSession.results.sortedByDescending { it.score }

        val text = sorted.joinToString("\n") {
            "Zawodnik ${it.playerNumber}: ${it.score} pkt"
        }

        txtRanking.text = text
    }
}
