package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class JudgeActivity : BaseActivity() {

    private lateinit var etPlayers: EditText
    private lateinit var etQuestions: EditText
    private lateinit var etTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        etPlayers = findViewById(R.id.etPlayers)
        etQuestions = findViewById(R.id.etQuestions)
        etTime = findViewById(R.id.etTime)

        attachValidator(etPlayers, 1, 10, "Liczba zawodników")
        attachValidator(etQuestions, 1, 30, "Liczba pytań")
        attachValidator(etTime, 1, 30, "Czas (minuty)")

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", etPlayers.text.toString().toInt())
            intent.putExtra("QUESTIONS", etQuestions.text.toString().toInt())
            intent.putExtra("TIME_SECONDS", etTime.text.toString().toInt() * 60)
            startActivity(intent)
        }
    }

    private fun attachValidator(
        editText: EditText,
        min: Int,
        max: Int,
        label: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) return

                val value = s.toString().toIntOrNull() ?: return

                when {
                    value < min -> {
                        Toast.makeText(this@JudgeActivity,
                            "$label min. $min", Toast.LENGTH_SHORT).show()
                        editText.setText(min.toString())
                        editText.setSelection(editText.text.length)
                    }
                    value > max -> {
                        Toast.makeText(this@JudgeActivity,
                            "$label max. $max", Toast.LENGTH_SHORT).show()
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
