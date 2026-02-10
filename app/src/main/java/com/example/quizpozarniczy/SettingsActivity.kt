package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter
import com.example.quizpozarniczy.util.QuizRepository

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnExportImport = findViewById<Button>(R.id.btnExportImport)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // 🔹 Inicjalizacja pytań lokalnych
        if (isOpiekun) {
            LocalQuestionsRepository.questions.clear()
            LocalQuestionsRepository.questions.addAll(DefaultLocalQuestions.questions)
        } else {
            LocalQuestionsRepository.questions.clear()
        }

        // 1️⃣ EDYCJA / B
        if (isOpiekun) {
            btnEditOrB.text = "EDYCJA PYTAŃ LOKALNYCH"
            btnEditOrB.setOnClickListener {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            }
        } else {
            btnEditOrB.text = "B"
            btnEditOrB.setOnClickListener {
                Toast.makeText(
                    this,
                    "Funkcja B (do implementacji)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 2️⃣ A – tymczasowo nieaktywne
        btnA.text = "A – do późniejszego wykorzystania"
        btnA.setOnClickListener {
            Toast.makeText(
                this,
                "Funkcja A będzie dostępna w późniejszej wersji",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 3️⃣ EXPORT / IMPORT
        if (isOpiekun) {
            btnExportImport.text = "UDOSTĘPNIJ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { exportLocalQuestions() }
        } else {
            btnExportImport.text = "POBIERZ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { importLocalQuestions() }
        }

        // 4️⃣ REGULAMIN
        btnRegulamin.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    // =========================
    // EXPORT – JSON
    // =========================
    private fun exportLocalQuestions() {
        val generalQuestions = QuizRepository.getQuestions(localCount = 0)
        val localQuestions = LocalQuestionsRepository.questions

        val uri = QuizExporter.createExportJson(
            context = this,
            generalQuestions = generalQuestions,
            localQuestions = localQuestions
        ) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Udostępnij pytania lokalne"))
    }

    // =========================
    // IMPORT – JSON
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
}
