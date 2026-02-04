package com.example.quizpozarniczy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.util.QuizExporter

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // EDYCJA PYTAŃ LOKALNYCH
        findViewById<Button>(R.id.btnEditLocalQuestions).setOnClickListener {
            startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
        }

        // REGULAMIN
        findViewById<Button>(R.id.btnRegulamin).setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }

        // UDOSTĘPNIJ TRYB NAUKI
        findViewById<Button>(R.id.btnShareLearningMode).setOnClickListener {

            // 1️⃣ Pytania ogólne (WSZYSTKIE, bez lokalnych)
            val generalQuestions: List<Question> =
                QuizRepository.getQuestions(localCount = 0)

            // 2️⃣ Pytania lokalne po edycji
            val localQuestions =
                DefaultLocalQuestions.questions

            // 3️⃣ Eksport do JSON → Uri
            val uri: Uri = QuizExporter
                .createExportJson(this, generalQuestions, localQuestions)
                ?: return@setOnClickListener

            // 4️⃣ Udostępnienie pliku
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Udostępnij Quiz Pożarniczy MDP – Tryb Nauki"
                )
            )
        }
    }
}
