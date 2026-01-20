package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnJudgePanel = findViewById<Button>(R.id.btnJudgePanel)

        btnJudgePanel.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }
    }
}
