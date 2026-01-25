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

        // 🔒 ekran zawsze włączony
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        // zdejmujemy flagę tylko gdy aplikacja idzie w tło
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
