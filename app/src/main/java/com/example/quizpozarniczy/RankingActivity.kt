package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val table = findViewById<TableLayout>(R.id.tableRanking)

        val sorted = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        sorted.forEachIndexed { index, result ->
            val row = TableRow(this)

            row.addView(cell("${index + 1}."))
            row.addView(cell(result.playerName))
            row.addView(cell("${result.score}"))
            row.addView(cell(formatTime(result.timeSeconds)))

            table.addView(row)
        }
    }

    private fun cell(text: String): TextView =
        TextView(this).apply {
            this.text = text
            setPadding(4, 4, 4, 4)
        }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
