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
            val intent = Intent(this, JudgeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        // ❌ nie trzymamy flagi gdy aplikacja idzie w tło
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        // 💀 zabicie Activity – nic nie ma prawa działać w tle
        finish()
    }
}
