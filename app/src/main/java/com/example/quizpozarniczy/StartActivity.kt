package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnJudge = findViewById<Button>(R.id.btnJudge)

        btnJudge.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }
    }
}
