package com.example.quizpozarniczy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = applicationId.endsWith(".opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnShareOrDownload = findViewById<Button>(R.id.btnShareOrDownload)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // Flavor-specific text / behavior
        if (isOpiekun) {
            // 🔹 Opiekun
            btnEditOrB.text = "Edycja pytań lokalnych"
            btnEditOrB.setOnClickListener {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            }

            btnA.text = "A"
            btnA.setOnClickListener {
                Toast.makeText(this, "Przycisk A (do implementacji)", Toast.LENGTH_SHORT).show()
            }

            btnShareOrDownload.text = "UDOSTĘPNIJ PYTANIA LOKALNE"
            btnShareOrDownload.setOnClickListener {
                exportLocalQuestions()
            }

            btnRegulamin.text = "Regulamin"
            btnRegulamin.setOnClickListener {
                startActivity(Intent(this, RegulaminActivity::class.java))
            }

        } else {
            // 🔹 Młodzież
            btnEditOrB.text = "B"
            btnEditOrB.setOnClickListener {
                Toast.makeText(this, "Funkcja B (do implementacji)", Toast.LENGTH_SHORT).show()
            }

            btnA.text = "A"
            btnA.setOnClickListener {
                Toast.makeText(this, "Przycisk A (do implementacji)", Toast.LENGTH_SHORT).show()
            }

            btnShareOrDownload.text = "POBIERZ PYTANIA LOKALNE"
            btnShareOrDownload.setOnClickListener {
                importLocalQuestions()
            }

            btnRegulamin.text = "Regulamin"
            btnRegulamin.setOnClickListener {
                startActivity(Intent(this, RegulaminActivity::class.java))
            }
        }
    }

    // =========================
    // EXPORT PYTAŃ – OPIEKUN
    // =========================
    private fun exportLocalQuestions() {
        val generalQuestions: List<Question> = QuizRepository.getQuestions(localCount = 0)
        val localQuestions: List<LocalQuestion> = LocalQuestionsRepository.questions

        val uri: Uri = QuizExporter.createExportJson(this, generalQuestions, localQuestions) ?: return

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Udostępnij pytania lokalne"))
    }

    // =========================
    // IMPORT PYTAŃ – MŁODZIEŻ
    // =========================
    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    // =========================
    // ODBIÓR IMPORTU
    // =========================
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

                Toast.makeText(this, "Zaimportowano ${localQuestions.size} pytań lokalnych", Toast.LENGTH_LONG).show()
            }
        }
    }
}
