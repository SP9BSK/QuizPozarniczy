package com.example.quizpozarniczy

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    // Skaner QR
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Skanowanie anulowane", Toast.LENGTH_SHORT).show()
        } else {
            // Import pytań z zeskanowanego JSON-a
            val (_, localQuestions) = QuizImporter.importQuizFromString(result.contents)
            if (localQuestions.isNotEmpty()) {
                LocalQuestionsRepository.questions.clear()
                LocalQuestionsRepository.questions.addAll(localQuestions)
                LocalQuestionsRepository.save(this)
            }

            Toast.makeText(
                this,
                "Zaimportowano ${localQuestions.size} pytań lokalnych",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // 🔥 Nie gasimy ekranu
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnExportImport = findViewById<Button>(R.id.btnExportImport)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // =========================
        // 1. EDYCJA / B
        // =========================
        if (isOpiekun) {
            btnEditOrB.text = "EDYCJA PYTAŃ LOKALNYCH"
            btnEditOrB.setOnClickListener {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            }
        } else {
            btnEditOrB.text = "B"
            btnEditOrB.setOnClickListener {
                Toast.makeText(this, "Funkcja B (do implementacji)", Toast.LENGTH_SHORT).show()
            }
        }

        // =========================
        // 2. A – QR
        // =========================
        btnA.text = if (isOpiekun) {
            "UDOSTĘPNIJ PYTANIA LOKALNE (QR)"
        } else {
            "POBIERZ PYTANIA LOKALNE (QR)"
        }

        btnA.setOnClickListener {
            if (isOpiekun) {
                shareLocalQuestionsAsQR()
            } else {
                scanLocalQuestionsQR()
            }
        }

        // =========================
        // 3. EXPORT / IMPORT – JSON
        // =========================
        if (isOpiekun) {
            btnExportImport.text = "UDOSTĘPNIJ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { exportLocalQuestions() }
        } else {
            btnExportImport.text = "POBIERZ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { importLocalQuestions() }
        }

        // =========================
        // 4. REGULAMIN
        // =========================
        btnRegulamin.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    // =========================
    // EXPORT – OPIEKUN (plik JSON)
    // =========================
    private fun exportLocalQuestions() {
        val generalQuestions = QuizRepository.getQuestions(localCount = 0)
        val localQuestions = LocalQuestionsRepository.questions

        if (localQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych do eksportu", Toast.LENGTH_LONG).show()
            return
        }

        val uri = QuizExporter.createExportJson(this, generalQuestions, localQuestions) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Udostępnij pytania lokalne"))
    }

    // =========================
    // IMPORT – MŁODZIEŻ (plik JSON)
    // =========================
    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val (_, localQuestions) = QuizImporter.importQuiz(this, inputStream)
                if (localQuestions.isNotEmpty()) {
                    LocalQuestionsRepository.questions.clear()
                    LocalQuestionsRepository.questions.addAll(localQuestions)
                    LocalQuestionsRepository.save(this)
                }

                Toast.makeText(
                    this,
                    "Zaimportowano ${localQuestions.size} pytań lokalnych",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // QR – TYLKO PYTANIA LOKALNE (dla OPIEKUNA)
    // =========================
    private fun shareLocalQuestionsAsQR() {
        val localQuestions = LocalQuestionsRepository.questions

        if (localQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych", Toast.LENGTH_LONG).show()
            return
        }

        val json = QuizExporter.localQuestionsToJson(localQuestions)

        try {
            val size = 800
            val bitmap = BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, size, size)

            val dialog = Dialog(this)
            val imageView = ImageView(this)
            imageView.setImageBitmap(bitmap)
            dialog.setContentView(imageView)
            dialog.setTitle("Kod QR – pytania lokalne")
            dialog.show()
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd QR: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // =========================
    // QR – Skaner (dla MŁODZIEŻY)
    // =========================
    private fun scanLocalQuestionsQR() {
        val options = ScanOptions().apply {
            setPrompt("Zeskanuj kod QR z pytaniami lokalnymi")
            setBeepEnabled(true)
            setOrientationLocked(true)
        }
        barcodeLauncher.launch(options)
    }
}
