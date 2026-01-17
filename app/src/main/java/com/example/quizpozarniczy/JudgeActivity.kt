package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)
    }

    fun startQuiz(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
