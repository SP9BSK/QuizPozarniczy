package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // 🔥 inicjalizacja lokalnych pytań
        LocalQuestionsRepository.init(this, loadDefaults = true)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Panel sędziego
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
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
