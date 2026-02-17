package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager

class LearningModeSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_mode_select)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        findViewById<Button>(R.id.btnGeneral).setOnClickListener {
            startLearning("GENERAL")
        }

        findViewById<Button>(R.id.btnLocal).setOnClickListener {
            startLearning("LOCAL")
        }
    }

    private fun startLearning(mode: String) {
        val intent = Intent(this, LearningActivity::class.java)
        intent.putExtra("LEARNING_MODE", mode)
        startActivity(intent)
    }
}
