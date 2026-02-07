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
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // -------------------------------
        // EDYCJA PYTAŃ LOKALNYCH / PRZYCISK B
        // -------------------------------
        findViewById<Button>(R.id.btnEditLocalQuestions).setOnClickListener {
            // W apce Młodzież jest tylko przycisk B → nic nie rób
            // W apce Opiekun:
            if (LocalQuestionsRepository.questions.isNotEmpty()) {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Brak pytań lokalnych do edycji",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // -------------------------------
        // UDOSTĘPNIJ PYTANIA LOKALNE – OPIEKUN
        // -------------------------------
        findViewById<Button>(R.id.btnShareLearningMode).setOnClickListener {

            val generalQuestions: List<Question> =
                QuizRepository.getQuestions(localCount = 0)

            val localQuestions = LocalQuestionsRepository.questions

            val uri: Uri = QuizExporter.createExportJson(
                this,
                generalQuestions,
                localQuestions
            ) ?: return@setOnClickListener

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Udostępnij Quiz Pożarniczy MDP – Pytania lokalne"
                )
            )
        }

        // -------------------------------
        // REGULAMIN
        // -------------------------------
        findViewById<Button>(R.id.btnRegulamin).setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    // =========================
    // IMPORT PYTAŃ LOKALNYCH – MŁODZIEŻ
    // =========================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return

            contentResolver.openInputStream(uri)?.use { inputStream ->
                val (_, localQuestions) = QuizImporter.importQuiz(this, inputStream)

                if (localQuestions.isNotEmpty()) {
                    LocalQuestionsRepository.questions.clear()
                    LocalQuestionsRepository.questions.addAll(localQuestions)
                    LocalQuestionsRepository.save(this)

                    Toast.makeText(
                        this,
                        "Zaimportowano ${localQuestions.size} pytań lokalnych",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Nie zaimportowano żadnych pytań lokalnych",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
