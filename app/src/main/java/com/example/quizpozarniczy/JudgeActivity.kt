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
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizRepository

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etLocalQuestions = findViewById<EditText>(R.id.etLocalQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnEditPlayers = findViewById<Button>(R.id.btnEditPlayers)
        val btnShareQuiz = findViewById<Button>(R.id.btnShareQuiz)

        setupLiveValidation(etPlayers, 1, 10)
        setupLiveValidation(etQuestions, 1, 30)
        setupLiveValidation(etLocalQuestions, 1, 3)
        setupLiveValidation(etTime, 1, 30)

        // ✏️ Edycja zawodników
        btnEditPlayers.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            QuizSession.reset(players)
            startActivity(Intent(this, EditPlayersActivity::class.java))
        }

        // ▶ Start quizu – nie resetujemy zawodników
        btnStart.setOnClickListener {
            val players = etPlayers.text.toString().toIntOrNull() ?: 1
            val questions = etQuestions.text.toString().toIntOrNull() ?: 1
            val local = etLocalQuestions.text.toString().toIntOrNull() ?: 1
            val timeSeconds = (etTime.text.toString().toIntOrNull() ?: 1) * 60

            if (local > questions) {
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
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("LOCAL_QUESTIONS", local)
            intent.putExtra("TIME_SECONDS", timeSeconds)
            startActivity(intent)
        }

        // 🔹 Udostępnij quiz dla 1 zawodnika
        btnShareQuiz.setOnClickListener {
            shareSinglePlayerQuiz()
        }
    }

    private fun setupLiveValidation(et: EditText, min: Int, max: Int) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val v = s?.toString()?.toIntOrNull() ?: return
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

    private fun shareSinglePlayerQuiz() {
        val playerName = QuizSession.playerNames.firstOrNull() ?: "Zawodnik 1"

        val mainQuestions = QuizRepository.getQuestions()
        val localCount = findViewById<EditText>(R.id.etLocalQuestions).text.toString().toIntOrNull() ?: 1
        val localQuestions = LocalQuestionsRepository.questions.take(localCount)

        val timeSeconds = (findViewById<EditText>(R.id.etTime).text.toString().toIntOrNull() ?: 1) * 60

        val uri = QuizExporter.createSinglePlayerQuizJson(
            context = this,
            playerName = playerName,
            generalQuestions = mainQuestions,
            localQuestions = localQuestions,
            timeSeconds = timeSeconds
        )

        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Udostępnij quiz"))
        } else {
            Toast.makeText(this, "Błąd podczas tworzenia pliku quizu", Toast.LENGTH_LONG).show()
        }
    }
}
