package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // =========================
        // 🔥 KLUCZOWA LINIA
        // =========================
        val isOpiekun = BuildConfig.APPLICATION_ID.contains("opiekun")

        // lokalne pytania:
        // - opiekun → ładuje domyślne
        // - młodzież → pusto, do importu
        LocalQuestionsRepository.init(
            context = this,
            loadDefaults = isOpiekun
        )

        // =========================
        // PRZYCISKI
        // =========================

        // A / Panel sędziego
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            if (isOpiekun) {
                startActivity(Intent(this, JudgeActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Funkcja A niedostępna w aplikacji młodzież",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Tryb nauki
        findViewById<Button>(R.id.btnLearn).setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
        }

        // Ustawienia
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
)
