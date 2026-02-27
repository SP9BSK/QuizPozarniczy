package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

        // 1. Pobieramy surowe wpisy
        val raw = ScanResultsStore.getAll()

        if (raw.isEmpty()) {
            tv.text = "Brak wyników"
            return
        }

        // 2. Parsujemy JSON → PlayerResult
        val results = raw.mapNotNull { line ->
    try {
        val obj = JSONObject(line)
        PlayerResult(
            playerNumber = 0,
            playerName = obj.getString("player"),
            score = obj.getInt("score"),
            total = obj.getInt("total"),
            wrongAnswers = emptyList(),   // ← poprawka
            timeSeconds = obj.getInt("time")
        )
    } catch (e: Exception) {
        null
    }
}



        // 3. Sortowanie jak w RankingActivity
        val sorted = results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

        // 4. Formatowanie tabeli
        val sb = StringBuilder()
        sb.append(String.format("%-4s %-15s %6s %8s\n", "M", "ZAWODNIK", "PKT", "CZAS"))
        sb.append("------------------------------------\n")

        sorted.forEachIndexed { index, r ->
           sb.append(
    String.format(
        "%-4d %-15s %6s %8s\n",
        index + 1,
        r.playerName.take(15),
        "${r.score}/${r.total}",
        formatTime(r.timeSeconds)
    )
)

        }

        tv.text = sb.toString()

        // 5. Zapis do CSV
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
        csv.append("Miejsce;Zawodnik;Punkty;Czas\n")

        results.forEachIndexed { index, r ->
           csv.append("${index + 1};${r.playerName};${r.score}/${r.total};${formatTime(r.timeSeconds)}\n")
        }

        val filename = "wyniki_quizu.csv"
        val file = File(getExternalFilesDir(null), filename)
        file.writeText(csv.toString())

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, file.toURI())
            putExtra(Intent.EXTRA_SUBJECT, "Wyniki quizu")
        }

        startActivity(Intent.createChooser(intent, "Udostępnij wyniki"))
    }
}
