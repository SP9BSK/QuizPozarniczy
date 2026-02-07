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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // ---- PRZYCISK 1: EDYCJA / B ----
        findViewById<Button?>(R.id.btnEditLocalQuestions)?.setOnClickListener {
            startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
        }

        findViewById<Button?>(R.id.btnB)?.setOnClickListener {
            Toast.makeText(this, "Przycisk B – do implementacji", Toast.LENGTH_SHORT).show()
        }

        // ---- PRZYCISK 2: A ----
        findViewById<Button?>(R.id.btnA)?.setOnClickListener {
            Toast.makeText(this, "Przycisk A – do implementacji", Toast.LENGTH_SHORT).show()
        }

        // ---- PRZYCISK 3: EXPORT / IMPORT ----
        findViewById<Button?>(R.id.btnExport)?.setOnClickListener {
            exportLocalQuestions()
        }

        findViewById<Button?>(R.id.btnImport)?.setOnClickListener {
            importLocalQuestions()
        }

        // ---- REGULAMIN ----
        findViewById<Button>(R.id.btnRegulamin).setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    private fun exportLocalQuestions() {
        val generalQuestions: List<Question> = QuizRepository.getQuestions(localCount = 0)
        val localQuestions: List<LocalQuestion> = LocalQuestionsRepository.questions

        val uri: Uri = QuizExporter.createExportJson(this, generalQuestions, localQuestions) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Udostępnij pytania lokalne"))
    }

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

            contentResolver.openInputStream(uri)?.use {
                val (_, localQuestions) = QuizImporter.importQuiz(this, it)

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
    }
}
