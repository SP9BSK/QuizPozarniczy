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

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
        private const val MAX_TIME_MINUTES = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        // WALIDACJA W TRAKCIE WPISYWANIA
        setupLiveValidation(etPlayers, 1, MAX_PLAYERS, "Zawodników")
        setupLiveValidation(etQuestions, 1, MAX_QUESTIONS, "Pytań")
        setupLiveValidation(etTime, 1, MAX_TIME_MINUTES, "Czas (min)")

        btnStart.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questions = etQuestions.text.toString().toIntOrNull() ?: 1
            val minutes = etTime.text.toString().toIntOrNull() ?: 1

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", minutes * 60)

            startActivity(intent)
        }
    }

    private fun setupLiveValidation(
        editText: EditText,
        min: Int,
        max: Int,
        label: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isEmpty()) return

                val value = text.toIntOrNull()
                if (value == null) {
                    editText.setText(min.toString())
                    editText.setSelection(editText.text.length)
                    return
                }

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

