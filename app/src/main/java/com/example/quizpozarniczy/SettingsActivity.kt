package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // ISTNIEJĄCA LOGIKA
        findViewById<Button>(R.id.btnEditLocalQuestions).setOnClickListener {
            startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
        }

        // REGULAMIN
        findViewById<Button>(R.id.btnRegulamin).setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }
}
