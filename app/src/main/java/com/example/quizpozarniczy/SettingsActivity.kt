package com.example.quizpozarniczy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val isOpiekun = packageName.contains(".opiekun")

        val btnPrimary = findViewById<Button>(R.id.btnPrimary)
        val btnShare = findViewById<Button>(R.id.btnShareLearningMode)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // =========================
        // 👨‍🚒 OPIEKUN
        // =========================
        if (isOpiekun) {

            btnPrimary.text = "Edycja pytań lokalnych"
            btnPrimary.setOnClickListener {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            }

            btnShare.text = "Udostępnij pytania lokalne"
            btnShare.setOnClickListener {

                // pytania ogólne (bez lokalnych)
                val generalQuestions: List<Question> =
                    QuizRepository.getQuestions(localCount = 0)

                // pytania lokalne opiekuna
                val localQuestions = LocalQuestionsRepository.questions

                val uri: Uri = QuizExporter
                    .createExportJson(this, generalQuestions, localQuestions)
                    ?: return@setOnClickListener

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        "Udostępnij pytania lokalne"
                    )
                )
            }
        }

        // =========================
        // 👦 MŁODZIEŻ
        // =========================
        else {

            btnPrimary.text = "B"
            btnPrimary.setOnClickListener {
                Toast.makeText(
                    this,
                    "Przycisk B – do wykorzystania później",
                    Toast.LENGTH_SHORT
                ).show()
            }

            btnShare.text = "Pobierz pytania lokalne"
            btnShare.setOnClickListener {

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                }
                startActivityForResult(intent, 1001)
            }
        }

        // =========================
        // REGULAMIN
        // =========================
        btnRegulamin.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    // =========================
    // IMPORT PYTAŃ LOKALNYCH – MŁODZIEŻ
    // =========================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return

            contentResolver.openInputStream(uri)?.use { inputStream ->
                val (_, localQuestions) =
                    QuizImporter.importQuiz(this, inputStream)

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
