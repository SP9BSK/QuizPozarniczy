package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    private lateinit var etPlayers: EditText
    private lateinit var etQuestions: EditText
    private lateinit var etLocalQuestions: EditText
    private lateinit var etTime: EditText

    private lateinit var txtPlayersDisplay: TextView

    private lateinit var btnStart: Button
    private lateinit var btnEditPlayers: Button
    private lateinit var btnShareQuiz: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        // Nie gaś ekranu
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Pobierz widoki
        etPlayers = findViewById(R.id.etPlayers)
        etQuestions = findViewById(R.id.etQuestions)
        etLocalQuestions = findViewById(R.id.etLocalQuestions)
        etTime = findViewById(R.id.etTime)

        // TextView wyświetlający listę zawodników
        txtPlayersDisplay = findViewById(R.id.txtPlayersDisplay)

        btnStart = findViewById(R.id.btnStart)
        btnEditPlayers = findViewById(R.id.btnEditPlayers)
        btnShareQuiz = findViewById(R.id.btnShareQuiz)

        // Walidacja na żywo dla pól liczbowych
        setupLiveValidation(etPlayers, 1, 10, "Zawodników")
        setupLiveValidation(etQuestions, 1, 30, "Pytań")
        setupLiveValidation(etLocalQuestions, 1, 3, "Pytań lokalnych")
        setupLiveValidation(etTime, 1, 30, "Czas")

        // Wyświetl aktualną listę zawodników
        updatePlayersDisplay()

        // Edycja zawodników
        btnEditPlayers.setOnClickListener {
            val playersCount = etPlayers.text.toString().toIntOrNull() ?: 1
            QuizSession.reset(playersCount)

            startActivity(Intent(this, EditPlayersActivity::class.java))
        }

        // Start quizu
        btnStart.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questionsTotal = etQuestions.text.toString().toIntOrNull() ?: 1
            val localQuestions = etLocalQuestions.text.toString().toIntOrNull() ?: 1
            val timeSeconds = (etTime.text.toString().toIntOrNull() ?: 1) * 60

            if (localQuestions > questionsTotal) {
                Toast.makeText(
                    this,
                    "Pytania lokalne nie mogą być większe niż ogółem",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            QuizSession.reset(players)

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questionsTotal)
            intent.putExtra("LOCAL_QUESTIONS", localQuestions)
            intent.putExtra("TIME_SECONDS", timeSeconds)

            startActivity(intent)
        }

        // Udostępnianie quizu (placeholder)
        btnShareQuiz.setOnClickListener {
            Toast.makeText(this, "Udostępnianie – wkrótce", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Odśwież listę zawodników po powrocie z EditPlayersActivity
        updatePlayersDisplay()
    }

    private fun updatePlayersDisplay() {
        if (QuizSession.playerNames.isNotEmpty()) {
            txtPlayersDisplay.text = "Zawodnicy: ${QuizSession.playerNames.joinToString(", ")}"
        } else {
            txtPlayersDisplay.text = "Brak zawodników"
        }
    }

    private fun setupLiveValidation(et: EditText, min: Int, max: Int, label: String) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return
                val v = s.toString().toIntOrNull() ?: return

                when {
                    v < min -> et.setText(min.toString())
                    v > max -> et.setText(max.toString())
                }
                et.setSelection(et.text.length)
            }

            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
    }
}
