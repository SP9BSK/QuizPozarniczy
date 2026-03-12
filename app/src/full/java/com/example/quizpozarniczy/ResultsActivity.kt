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
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent


class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val tv = findViewById<TextView>(R.id.tvResults)
        val btnSave = findViewById<Button>(R.id.btnSaveResults)
        val btnBack = findViewById<Button>(R.id.btnBackToMain)

btnBack.setOnClickListener {
    ScanResultsStore.clear()   // <‑‑ czyści listę wyników

    val intent = Intent(this, StartActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    finish()
}




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
            setText("wyniki_quizu.txt")
        }

        AlertDialog.Builder(this)
            .setTitle("Nazwa pliku")
            .setView(input)
            .setPositiveButton("Zapisz") { _, _ ->
                var name = input.text.toString().trim()
                if (!name.endsWith(".txt", ignoreCase = true)) {
                    name += ".txt"
                }
                saveTextFile(results, name)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    private fun saveTextFile(results: List<PlayerResult>, filename: String) {
        val sb = StringBuilder()

        val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

        sb.append("Quiz Pożarniczy MDP\n")
        sb.append("Wyniki z dnia: $date\n\n")
        sb.append(String.format("%-3s %-12s %-7s %-6s\n", "M", "ZAWODNIK", "WYNIK", "CZAS"))
        sb.append("------------------------------------\n")

        results.forEachIndexed { index, r ->
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

        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloads.exists()) downloads.mkdirs()

        val file = File(downloads, filename)
        file.writeText(sb.toString())

        MediaScannerConnection.scanFile(
            this,
            arrayOf(file.absolutePath),
            arrayOf("text/plain"),
            null
        )

        Toast.makeText(this, "Zapisano plik tekstowy w Pobrane:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}
