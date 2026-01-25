package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
override fun onPause() {
    super.onPause()
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

        val btnJudge = findViewById<Button>(R.id.btnJudge)

        btnJudge.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }
    }
}
