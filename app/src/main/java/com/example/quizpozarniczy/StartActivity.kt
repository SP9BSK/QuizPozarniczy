package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class StartActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // 🔹 inicjalizacja repozytorium
        LocalQuestionsRepository.init(
            context = this,
            loadDefaults = isOpiekun
        )

        // 🔹 PRZYCISK A / PANEL SĘDZIEGO
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            if (isOpiekun) {
                startActivity(Intent(this, JudgeActivity::class.java))
            } else {
                // Tryb gracza – start quizu z lokalnymi pytaniami
                QuizSession.playerNames.clear()
                QuizSession.playerNames.add("Gracz 1")

                QuizSession.questions.clear()
                QuizSession.questions.addAll(LocalQuestionsRepository.toQuizQuestions(5))

                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("TIME_SECONDS", 60) // domyślny czas
                startActivity(intent)
            }
        }

        // 🔹 TRYB NAUKI
        findViewById<Button>(R.id.btnLearn).setOnClickListener {
            startActivity(Intent(this, LearningModeSelectActivity::class.java))
        }

        // 🔹 USTAWIENIA
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
