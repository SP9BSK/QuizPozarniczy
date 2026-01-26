package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class StartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
        }
    }
}
