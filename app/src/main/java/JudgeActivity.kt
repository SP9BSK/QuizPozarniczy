package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.util.ResultStore

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)
        ResultStore.results.clear()
    }

    fun startQuiz(v: View) {
        val players = findViewById<EditText>(R.id.etPlayers).text.toString().toInt()
        val time = findViewById<EditText>(R.id.etTime).text.toString().toInt()

        val i = Intent(this, QuizActivity::class.java)
        i.putExtra("PLAYERS", players)
        i.putExtra("PLAYER_INDEX", 1)
        i.putExtra("TIME", time)
        startActivity(i)
        finish()
    }
}
