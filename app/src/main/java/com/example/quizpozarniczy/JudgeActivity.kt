package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        // 🔒 ekran zawsze włączony
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        setupLiveValidation(etPlayers, 1, 10, "Zawodników")
        setupLiveValidation(etQuestions, 1, 30, "Pytań")
        setupLiveValidation(etTime, 1, 30, "Czas (min)")

        btnStart.setOnClickListener {
            val players = etPlayers.text.toString().toInt()
            val questions = etQuestions.text.toString().toInt()
            val timeSeconds = etTime.text.toString().toInt() * 60

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", timeSeconds) // ✅ KLUCZ ZGODNY

            startActivity(intent)
        }
    }

    private fun setupLiveValidation(
        et: EditText,
        min: Int,
        max: Int,
        label: String
    ) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return

                val value = s.toString().toIntOrNull() ?: return

                when {
                    value < min -> {
                        Toast.makeText(
                            this@JudgeActivity,
                            "$label min $min",
                            Toast.LENGTH_SHORT
                        ).show()
                        et.setText(min.toString())
                        et.setSelection(et.text.length)
                    }
                    value > max -> {
                        Toast.makeText(
                            this@JudgeActivity,
                            "$label max $max",
                            Toast.LENGTH_SHORT
                        ).show()
                        et.setText(max.toString())
                        et.setSelection(et.text.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
    }
}
