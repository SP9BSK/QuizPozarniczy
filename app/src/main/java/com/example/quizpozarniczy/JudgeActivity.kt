package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        // 🔥 WALIDACJA NATYCHMIASTOWA
        attachValidator(etPlayers, 1, 10, "Liczba zawodników")
        attachValidator(etQuestions, 1, 30, "Liczba pytań")
        attachValidator(etTime, 1, 30, "Czas (minuty)")

        btnStart.setOnClickListener {

            val players = etPlayers.text.toString().toInt()
            val questions = etQuestions.text.toString().toInt()
            val minutes = etTime.text.toString().toInt()

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", minutes * 60)

            startActivity(intent)
        }
    }

    // ================= WALIDATOR LIVE =================

    private fun attachValidator(
        editText: EditText,
        min: Int,
        max: Int,
        label: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return

                val value = s.toString().toIntOrNull() ?: return

                when {
                    value < min -> {
                        Toast.makeText(
                            this@JudgeActivity,
                            "$label nie może być mniejsze niż $min",
                            Toast.LENGTH_SHORT
                        ).show()
                        editText.setText(min.toString())
                        editText.setSelection(editText.text.length)
                    }

                    value > max -> {
                        Toast.makeText(
                            this@JudgeActivity,
                            "$label nie może być większe niż $max",
                            Toast.LENGTH_SHORT
                        ).show()
                        editText.setText(max.toString())
                        editText.setSelection(editText.text.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
