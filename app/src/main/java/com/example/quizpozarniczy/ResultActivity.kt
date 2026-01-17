package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.util.ResultStore

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tv = findViewById<TextView>(R.id.tvResults)

        val sorted = ResultStore.results.sortedByDescending { it.score }
        tv.text = sorted.joinToString("\n") {
            "Zawodnik ${it.playerNumber}: ${it.score} pkt"
        }
    }

    fun restart(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
