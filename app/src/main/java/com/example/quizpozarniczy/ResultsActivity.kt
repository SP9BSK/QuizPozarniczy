package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val txtResults = findViewById<TextView>(R.id.txtResults)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val results = intent.getStringExtra("RESULTS") ?: ""
        txtResults.text = results

        btnBack.setOnClickListener {
            finish()
        }
    }
}
