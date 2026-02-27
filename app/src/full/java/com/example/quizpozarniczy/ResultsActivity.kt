package com.example.quizpozarniczy

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import android.media.MediaScannerConnection


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

        val sorted = results.sortedWith(
            compareByDescending<PlayerResult> { it.score }
                .thenBy { it.timeSeconds }
        )

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
            showFilenameDialog(sorted)
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun showFilenameDialog(results: List<PlayerResult>) {
        val input = EditText(this).apply {
            setText("wyniki_quizu.csv")
        }

        AlertDialog.Builder(this)
            .setTitle("Nazwa pliku")
            .setView(input)
            .setPositiveButton("Zapisz") { _, _ ->
                var name = input.text.toString().trim()
                if (!name.endsWith(".csv", ignoreCase = true)) {
                    name += ".csv"
                }
                saveCsvToDocuments(results, name)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    private fun saveCsvToDocuments(results: List<PlayerResult>, filename: String) {
    val csv = StringBuilder()
    csv.append("Miejsce;Zawodnik;Wynik;Czas\n")

    results.forEachIndexed { index, r ->
        csv.append("${index + 1};${r.playerName};${r.score}/${r.total};${formatTime(r.timeSeconds)}\n")
    }

    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    if (!documentsDir.exists()) documentsDir.mkdirs()

    val file = File(documentsDir, filename)
    file.writeText(csv.toString())

    // Powiadom system, żeby plik pojawił się w „Dokumenty”
    MediaScannerConnection.scanFile(
        this,
        arrayOf(file.absolutePath),
        arrayOf("text/csv"),
        null
    )

    Toast.makeText(
        this,
        "Zapisano w Dokumenty:\n${file.absolutePath}",
        Toast.LENGTH_LONG
    ).show()
}

}
