package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.QuizActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)
    }

    fun startQuiz(v: View) {
        val i = Intent(this, QuizActivity::class.java)
        i.putExtra("TIME", 300)
        startActivity(i)
    }
}
