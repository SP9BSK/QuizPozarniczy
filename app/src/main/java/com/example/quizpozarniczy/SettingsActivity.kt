package com.example.quizpozarniczy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter

class SettingsActivity : AppCompatActivity() {

    private val IMPORT_JSON_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        findViewById<Button>(R.id.btnEditLocalQuestions).setOnClickListener {
            startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
        }

        findViewById<Button>(R.id.btnRegulamin).setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }

        findViewById<Button>(R.id.btnShareLearningMode).setOnClickListener {
            if (isOpiekunApp()) {
                exportLocalQuestions()
            } else {
                importLocalQuestions()
            }
        }
    }

    // =========================
    // OPIEKUN – EKSPORT
    // =========================
    private fun exportLocalQuestions() {

        val localQuestions = LocalQuestionsRepository.questions

        if (localQuestions.isEmpty()) {
            Toast.makeText(
                this,
                "Brak pytań lokalnych do udostępnienia",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val uri: Uri = QuizExporter
            .createExportJson(this, emptyList(), localQuestions)
            ?: return

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Udostępnij pytania lokalne – Quiz Pożarniczy MDP"
            )
        )
    }

    // =========================
    // MŁODZIEŻ – IMPORT
    // =========================
    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, IMPORT_JSON_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMPORT_JSON_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return

            val inputStream = contentResolver.openInputStream(uri) ?: return

            val (_, localQuestions) =
                QuizImporter.importQuiz(this, inputStream)

            // ⬅️ NADPISUJEMY pytania lokalne
            LocalQuestionsRepository.questions.clear()
            LocalQuestionsRepository.questions.addAll(localQuestions)
            LocalQuestionsRepository.save(this)

            Toast.makeText(
                this,
                "Zaimportowano ${localQuestions.size} pytań lokalnych",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isOpiekunApp(): Boolean {
        return packageName.contains(".opiekun")
    }
}
