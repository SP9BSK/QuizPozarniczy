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

    companion object {
        private const val REQUEST_EDIT_PLAYERS = 100
    }

    private lateinit var txtPlayers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etLocalQuestions = findViewById<EditText>(R.id.etLocalQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)

        txtPlayers = findViewById(R.id.txtPlayersDisplay)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnEditPlayers = findViewById<Button>(R.id.btnEditPlayers)
        val btnShareQuiz = findViewById<Button>(R.id.btnShareQuiz)

        // Pokazujemy domyślne listy graczy
        updatePlayersDisplay()

        setupLiveValidation(etPlayers, 1, 10, "Zawodników")
        setupLiveValidation(etQuestions, 1, 30, "Pytań")
        setupLiveValidation(etLocalQuestions, 1, 3, "Pytań lokalnych")
        setupLiveValidation(etTime, 1, 30, "Czas")

        btnEditPlayers.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            QuizSession.ensurePlayers(players)

            // Startujemy aktywność edycji zawodników
            val intent = Intent(this, EditPlayersActivity::class.java)
            startActivityForResult(intent, REQUEST_EDIT_PLAYERS)
        }

        btnShareQuiz.setOnClickListener {
            Toast.makeText(this, "Udostępnianie – wkrótce", Toast.LENGTH_SHORT).show()
        }

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

            QuizSession.ensurePlayers(players)

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questionsTotal)
            intent.putExtra("LOCAL_QUESTIONS", localQuestions)
            intent.putExtra("TIME_SECONDS", timeSeconds)

            startActivity(intent)
        }
    }

    // Obsługa powrotu z EditPlayersActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PLAYERS && resultCode == RESULT_OK) {
            updatePlayersDisplay()
        }
    }

    // Funkcja odświeżająca widok zawodników
    private fun updatePlayersDisplay() {
        txtPlayers.text = QuizSession.playerNames.joinToString("\n")
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
