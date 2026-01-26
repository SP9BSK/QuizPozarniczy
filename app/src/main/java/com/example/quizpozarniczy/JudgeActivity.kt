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
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)

        setupLiveValidation(etPlayers, 1, 10, "Zawodników")
        setupLiveValidation(etQuestions, 1, 30, "Pytań")
        setupLiveValidation(etTime, 1, 30, "Czas")

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val players = etPlayers.text.toString().toInt()
            val questions = etQuestions.text.toString().toInt()
            val time = etTime.text.toString().toInt() * 60

            val i = Intent(this, QuizActivity::class.java)
            i.putExtra("PLAYERS", players)
            i.putExtra("QUESTIONS", questions)
            i.putExtra("TIME", time)
            startActivity(i)
        }
    }

    private fun setupLiveValidation(et: EditText, min: Int, max: Int, label: String) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return
                val v = s.toString().toIntOrNull() ?: return
                when {
                    v < min -> {
                        Toast.makeText(this@JudgeActivity, "$label min $min", Toast.LENGTH_SHORT).show()
                        et.setText(min.toString())
                    }
                    v > max -> {
                        Toast.makeText(this@JudgeActivity, "$label max $max", Toast.LENGTH_SHORT).show()
                        et.setText(max.toString())
                    }
                }
                et.setSelection(et.text.length)
            }
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
    }
}
