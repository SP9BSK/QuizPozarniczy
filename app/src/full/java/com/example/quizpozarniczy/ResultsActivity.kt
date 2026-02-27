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
import java.io.FileOutputStream
import android.media.MediaScannerConnection
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.xlsx4j.sml.*

import java.text.SimpleDateFormat
import java.util.*

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
            setText("wyniki_quizu.xlsx")
        }

        AlertDialog.Builder(this)
            .setTitle("Nazwa pliku")
            .setView(input)
            .setPositiveButton("Zapisz") { _, _ ->
                var name = input.text.toString().trim()
                if (!name.endsWith(".xlsx", ignoreCase = true)) {
                    name += ".xlsx"
                }
                saveXlsx(results, name)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    private fun saveXlsx(results: List<PlayerResult>, filename: String) {
        val pkg = SpreadsheetMLPackage.createPackage()
        val sheet = pkg.workbookPart.worksheetParts[0]
        val ws = sheet.jaxbElement

        val sheetData = ws.sheetData ?: CT_SheetData().also { ws.sheetData = it }

        val sharedStrings = pkg.workbookPart.sharedStrings ?: SharedStrings().also {
            pkg.workbookPart.sharedStrings = it
        }

        fun addString(value: String): Int {
            val si = CTSst.Si().apply { t = value }
            sharedStrings.jaxbElement.si.add(si)
            return sharedStrings.jaxbElement.si.size - 1
        }

        fun addRow(rowIndex: Int, values: List<String>) {
            val row = CT_Row().apply { r = rowIndex.toLong() }
            values.forEachIndexed { colIndex, value ->
                val cell = CT_Cell().apply {
                    r = "${('A' + colIndex)}$rowIndex"
                    t = ST_CellType.S
                    v = addString(value).toString()
                }
                row.c.add(cell)
            }
            sheetData.row.add(row)
        }

        val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

        addRow(1, listOf("Quiz Pożarniczy MDP"))
        addRow(2, listOf("Wyniki z dnia: $date"))
        addRow(3, listOf("")) // odstęp
        addRow(4, listOf("Miejsce", "Zawodnik", "Wynik", "Czas"))

        results.forEachIndexed { index, r ->
            addRow(
                5 + index,
                listOf(
                    (index + 1).toString(),
                    r.playerName,
                    "${r.score}/${r.total}",
                    formatTime(r.timeSeconds)
                )
            )
        }

        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloads.exists()) downloads.mkdirs()

        val file = File(downloads, filename)
        FileOutputStream(file).use { pkg.save(it) }

        MediaScannerConnection.scanFile(
            this,
            arrayOf(file.absolutePath),
            arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
            null
        )

        Toast.makeText(this, "Zapisano XLSX w Pobrane:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}
