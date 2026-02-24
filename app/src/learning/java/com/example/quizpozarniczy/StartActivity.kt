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

        // MŁODZIEŻ → nie ładuje domyślnych pytań
        LocalQuestionsRepository.init(
            context = this,
            loadDefaults = false
        )

        // Pobierz quiz (skanowanie QR)
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            startActivity(Intent(this, ReceiveQuizActivity::class.java))
        }

        // Tryb nauki
        findViewById<Button>(R.id.btnLearn).setOnClickListener {
            startActivity(Intent(this, LearningModeSelectActivity::class.java))
        }

        // Ustawienia
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
