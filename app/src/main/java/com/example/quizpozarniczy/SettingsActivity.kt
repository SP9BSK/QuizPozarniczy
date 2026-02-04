package com.example.quizpozarniczy

import android.content.Intent
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
            // Pobieramy wszystkie pytania: ogólne + lokalne
            val questions: List<Question> = QuizRepository.getQuestions(DefaultLocalQuestions.questions.size)

            // Generujemy JSON i udostępniamy
            val shareIntent = QuizExporter.createExportJson(this, questions)
            startActivity(Intent.createChooser(shareIntent, "Udostępnij Quiz Pożarniczy MDP"))
        }
    }
}
