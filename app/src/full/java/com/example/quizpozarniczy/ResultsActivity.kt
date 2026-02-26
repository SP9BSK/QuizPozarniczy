package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val tv = findViewById<TextView>(R.id.tvResults)
        val text = ScanResultsStore.getAll().joinToString("\n")
        tv.text = if (text.isEmpty()) "Brak wyników" else text
    }
}
