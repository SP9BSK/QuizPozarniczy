package com.example.quizpozarniczy

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        txtRanking.typeface = Typeface.MONOSPACE

        val results = ResultStore.getResultsSorted()

        val sb = StringBuilder()
        sb.append(
            String.format(
                "%-4s %-15s %6s %8s\n",
                "M", "ZAWODNIK", "PKT", "CZAS"
            )
        )
        sb.append("----------------------------------------\n")

        results.forEachIndexed { index, result ->
            sb.append(
                String.format(
                    "%-4d %-15s %6d %8s\n",
                    index + 1,
                    result.playerName.take(15),
                    result.points,
                    result.timeFormatted
                )
            )
        }

        txtRanking.text = sb.toString()
    }
}
