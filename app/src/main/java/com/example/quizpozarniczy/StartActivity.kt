package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
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

        LocalQuestionsRepository.init(
            context = this,
            loadDefaults = isOpiekun
        )

        // =========================
        // PRZYCISKI
        // =========================
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }

        findViewById<Button>(R.id.btnLearn).setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
