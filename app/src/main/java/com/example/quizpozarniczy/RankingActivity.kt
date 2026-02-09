package com.example.quizpozarniczy

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val table = findViewById<TableLayout>(R.id.tableRanking)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)

        // sortowanie
        val sorted = QuizSession.results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        // wiersze tabeli
        sorted.forEachIndexed { index, result ->
            val row = TableRow(this)

            row.addView(cell("${index + 1}.", Gravity.CENTER))
            row.addView(nameCell(result.playerName)) // ← STAŁA SZEROKOŚĆ
            row.addView(cell("${result.score}", Gravity.CENTER))
            row.addView(cell(formatTime(result.timeSeconds), Gravity.CENTER))

            table.addView(row)
        }

        // POWRÓT DO PANELU SĘDZIEGO + RESET
        btnBack.setOnClickListener {
            QuizSession.resetAll()

            val intent = Intent(this, JudgeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    // ---------- POMOCNICZE ----------

    private fun cell(text: String, gravity: Int): TextView =
        TextView(this).apply {
            this.text = text
            this.gravity = gravity
            setPadding(12, 8, 12, 8)
            textSize = 16f
        }

    private fun nameCell(text: String): TextView =
        TextView(this).apply {
            this.text = text
            setPadding(12, 8, 12, 8)
            textSize = 16f
            typeface = Typeface.MONOSPACE
            width = dpToPx(180)   // ≈ 15 znaków
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
