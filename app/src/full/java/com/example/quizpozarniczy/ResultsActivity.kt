package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val tv = findViewById<TextView>(R.id.tvResults)
        val btnSave = findViewById<Button>(R.id.btnSaveResults)

        val raw = ScanResultsStore.getAll()

        if (raw.isEmpty()) {
            tv.text = "Brak wyników"
            return
        }

        // Parsowanie JSON → PlayerResult
        val results = raw.mapNotNull { line ->
            try {
                val obj = JSONObject(line)
                PlayerResult(
                    playerNumber = 0,
                    playerName = obj.getString("player"),
                    score = obj.getInt("score"),
                    total = obj.getInt("total"),
                    wrongAnswers = emptyList(),
                    timeSeconds = obj.getInt("time")
                )
            } catch (e: Exception) {
                null
            }
        }

        // Sortowanie
        val sorted = results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        // Formatowanie tabeli
        val sb = StringBuilder()
        sb.append(String.format("%-3s %-12s %-7s %-6s\n", "M", "ZAWODNIK", "WYNIK", "CZAS"))
        sb.append("------------------------------------\n")

        sorted.forEachIndexed { index, r ->
            sb.append(
                String.format(
                    "%-3d %-12s %-7s %-6s\n",
                    index + 1,
                    r.playerName.take(12),
                    "${r.score}/${r.total}",
                    formatTime(r.timeSeconds)
                )
            )
        }

        tv.text = sb.toString()

        btnSave.setOnClickListener {
            saveCsv(sorted)
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun saveCsv(results: List<PlayerResult>) {
        val csv = StringBuilder()
        csv.append("Miejsce;Zawodnik;Wynik;Czas\n")

        results.forEachIndexed { index, r ->
            csv.append("${index + 1};${r.playerName};${r.score}/${r.total};${formatTime(r.timeSeconds)}\n")
        }

        val filename = "wyniki_quizu.csv"
        val file = File(getExternalFilesDir(null), filename)
        file.writeText(csv.toString())

        Toast.makeText(
            this,
            "Zapisano plik:\n${file.absolutePath}",
            Toast.LENGTH_LONG
        ).show()
    }
}
