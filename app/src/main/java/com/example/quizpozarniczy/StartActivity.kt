package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class StartActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = applicationId.endsWith(".opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 🔥 inicjalizacja lokalnych pytań
        LocalQuestionsRepository.init(this, loadDefaults = isOpiekun)

        val btnJudge = findViewById<Button>(R.id.btnJudge)
        val btnLearn = findViewById<Button>(R.id.btnLearn)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val txtFlavor = findViewById<TextView>(R.id.txtFlavor) // dodatkowy TextView pod przyciskami

        // flavor-specific text
        txtFlavor.text = if (isOpiekun) "Opiekun" else "Młodzież"

        // Panel sędziego / A
        btnJudge.text = if (isOpiekun) "PANEL SĘDZIEGO" else "A"
        btnJudge.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }

        // Tryb nauki
        btnLearn.setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
        }

        // Ustawienia
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
