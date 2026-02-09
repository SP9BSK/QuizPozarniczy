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
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // 🔥 nie gasimy ekranu
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
        // 2. A – QR (FLAVORS 🔥)
        // =========================
        btnA.text = getString(R.string.share_local_questions_label)
        btnA.setOnClickListener {
            if (isOpiekun) {
                shareLocalQuestionsAsQR()
            } else {
                scanLocalQuestionsQR()
            }
        }

        // =========================
        // 3. EXPORT / IMPORT – PLIK JSON
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
    // EXPORT – PLIK
    // =========================
    private fun exportLocalQuestions() {
        val generalQuestions = QuizRepository.getQuestions(localCount = 0)
        val localQuestions = LocalQuestionsRepository.questions

        if (localQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych", Toast.LENGTH_LONG).show()
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
    // IMPORT – PLIK
    // =========================
    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    // =========================
    // QR – OPIEKUN
    // =========================
    private fun shareLocalQuestionsAsQR() {
        val localQuestions = LocalQuestionsRepository.questions
        if (localQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych", Toast.LENGTH_LONG).show()
            return
        }

        val json = QuizExporter.localQuestionsToJson(localQuestions)

        try {
            val bitmap = BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, 800, 800)

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
    // SKANUJ QR – MŁODZIEŻ
    // =========================
    private fun scanLocalQuestionsQR() {
        IntentIntegrator(this).apply {
            setOrientationLocked(true)
            setPrompt("Zeskanuj kod QR pytań lokalnych")
            setBeepEnabled(true)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // =========================
        // IMPORT PLIKU JSON
        // =========================
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val (_, localQuestions) = QuizImporter.importQuiz(this, inputStream)

                if (localQuestions.isNotEmpty()) {
                    LocalQuestionsRepository.questions.clear()
                    LocalQuestionsRepository.questions.addAll(localQuestions)
                    LocalQuestionsRepository.save(this)
                }

                Toast.makeText(this, "Zaimportowano ${localQuestions.size} pytań lokalnych", Toast.LENGTH_LONG).show()
            }
        }

        // =========================
        // ODCZYT QR
        // =========================
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { json ->
            try {
                val (_, localQuestions) = QuizImporter.importQuizFromString(json)
                if (localQuestions.isNotEmpty()) {
                    LocalQuestionsRepository.questions.clear()
                    LocalQuestionsRepository.questions.addAll(localQuestions)
                    LocalQuestionsRepository.save(this)
                }
                Toast.makeText(this, "Zaimportowano ${localQuestions.size} pytań lokalnych", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Błąd przy imporcie QR: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
