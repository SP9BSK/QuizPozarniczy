package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class JudgeActivity : AppCompatActivity() {

    companion object {
        private const val MAX_PLAYERS = 10
        private const val MAX_QUESTIONS = 30
        private const val MAX_MINUTES = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        // 🔒 ekran zawsze włączony
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        // ✅ WALIDACJA NATYCHMIAST PO WPISANIU
        attachValidator(etPlayers, 1, MAX_PLAYERS, "Liczba zawodników")
        attachValidator(etQuestions, 1, MAX_QUESTIONS, "Liczba pytań")
        attachValidator(etTime, 1, MAX_MINUTES, "Czas (minuty)")

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

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // ===== WALIDATOR =====
    private fun attachValidator(
        editText: EditText,
        min: Int,
        max: Int,
        label: String
    ) {
        editText.doAfterTextChanged {
            val value = it.toString().toIntOrNull()

            when {
                value == null || value < min -> {
                    Toast.makeText(
                        this,
                        "$label nie może być mniejsze niż $min",
                        Toast.LENGTH_SHORT
                    ).show()
                    editText.setText(min.toString())
                    editText.setSelection(editText.text.length)
                }
                value > max -> {
                    Toast.makeText(
                        this,
                        "$label nie może być większe niż $max",
                        Toast.LENGTH_SHORT
                    ).show()
                    editText.setText(max.toString())
                    editText.setSelection(editText.text.length)
                }
            }
        }
    }
}
