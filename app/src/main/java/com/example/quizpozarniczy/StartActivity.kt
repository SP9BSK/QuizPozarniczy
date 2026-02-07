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

        // Ładujemy layout (jeden layout dla obu flavorów)
        setContentView(R.layout.activity_start)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 🔥 inicjalizacja lokalnych pytań
        // W apce opiekun domyślnie wczytujemy pytania lokalne
        val loadDefaults = BuildConfig.APPLICATION_ID.endsWith(".opiekun")
         LocalQuestionsRepository.init(this, loadDefaults = loadDefaults)


        val btnJudge = findViewById<Button>(R.id.btnJudge)
        val btnLearn = findViewById<Button>(R.id.btnLearn)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // Panel sędziego / A – już ustawione w XML
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
