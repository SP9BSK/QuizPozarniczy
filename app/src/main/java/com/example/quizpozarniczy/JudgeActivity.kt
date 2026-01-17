package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)
    }

    fun startQuiz(v: View) {
        val players = 2
        val time = 300

        val i = Intent(this, QuizActivity::class.java)
        i.putExtra("PLAYERS", players)
        i.putExtra("TIME", time)
        i.putExtra("PLAYER_INDEX", 1)
        startActivity(i)
    }
}
