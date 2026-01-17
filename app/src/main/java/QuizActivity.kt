package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.util.QuizRepository

class QuizActivity : AppCompatActivity() {

    override fun onBackPressed() {} // BLOKADA COFANIA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val questions = QuizRepository.load(this)
        startTimer(intent.getIntExtra("TIME", 300))
    }

    private fun startTimer(sec: Int) {
        object : CountDownTimer(sec * 1000L, 1000) {
            override fun onTick(ms: Long) {}
            override fun onFinish() {}
        }.start()
    }
}
