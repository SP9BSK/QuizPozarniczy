package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("players", etPlayers.text.toString().toInt())
            intent.putExtra("questions", etQuestions.text.toString().toInt())
            intent.putExtra("time", etTime.text.toString().toInt()) // MINUTY
            startActivity(intent)
        }
    }
}
