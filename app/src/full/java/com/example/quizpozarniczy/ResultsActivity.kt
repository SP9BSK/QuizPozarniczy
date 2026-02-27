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
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook

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
                saveXlsxToDownloads(results, name)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    private fun saveXlsxToDownloads(results: List<PlayerResult>, filename: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Wyniki")

        // STYLE: nagłówek
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }

        // STYLE: komórki
        val cellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }

        // Nagłówki
        val headerRow = sheet.createRow(0)
        val headers = listOf("Miejsce", "Zawodnik", "Wynik", "Czas")

        headers.forEachIndexed { i, title ->
            val cell = headerRow.createCell(i)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }

        // Dane
        results.forEachIndexed { index, r ->
            val row = sheet.createRow(index + 1)

            val scoreText = "${r.score}/${r.total}"  // poprawne 10/12

            val values = listOf(
                (index + 1).toString(),
                r.playerName,
                scoreText,
                formatTime(r.timeSeconds)
            )

            values.forEachIndexed { i, value ->
                val cell = row.createCell(i)
                cell.setCellValue(value)
                cell.cellStyle = cellStyle
            }
        }

        // Auto szerokość kolumn
        for (i in headers.indices) sheet.autoSizeColumn(i)

        // Zapis do Pobrane
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloads.exists()) downloads.mkdirs()

        val file = File(downloads, filename)
        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()

        MediaScannerConnection.scanFile(
            this,
            arrayOf(file.absolutePath),
            arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
            null
        )

        Toast.makeText(this, "Zapisano XLSX w Pobrane:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}
