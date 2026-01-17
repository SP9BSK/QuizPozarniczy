package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val max = intent.getIntExtra("max", 0)

        val txtResult = findViewById<TextView>(R.id.txtResult)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)

        txtResult.text = "Wynik: $score / $max"

        btnBack.setOnClickListener {
            val intent = Intent(this, JudgeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
