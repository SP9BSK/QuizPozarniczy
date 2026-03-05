package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.Question

class LearningLocalActivity : AppCompatActivity() {

    private lateinit var txtProgress: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnSaveExit: Button

    private val prefsName = "LOCAL_LEARNING_PROGRESS"
    private val keySolved = "solved_ids_local"

    private val allQuestions = mutableListOf<Question>()
    private val solvedIds = mutableSetOf<String>()
    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        txtProgress = findViewById(R.id.txtProgress)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnSaveExit = findViewById(R.id.btnSaveExit)

        loadProgress()

        allQuestions.clear()
        allQuestions.addAll(LocalQuestionsRepository.toQuizQuestions(Int.MAX_VALUE))

        if (allQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        nextQuestion()

        btnA.setOnClickListener { checkAnswer(0) }
        btnB.setOnClickListener { checkAnswer(1) }
        btnC.setOnClickListener { checkAnswer(2) }

        btnSaveExit.setOnClickListener {
            saveProgress()
            finish()
        }
    }

    private fun loadProgress() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        solvedIds.clear()
        solvedIds.addAll(prefs.getStringSet(keySolved, emptySet()) ?: emptySet())
    }

    private fun saveProgress() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        prefs.edit().putStringSet(keySolved, solvedIds).apply()
    }

    private fun nextQuestion() {
        txtProgress.text = "Opanowane: ${solvedIds.size} / ${allQuestions.size}"

        val remaining = allQuestions.filter { q -> q.id.toString() !in solvedIds }

        if (remaining.isEmpty()) {
            showFinishedDialog()
            return
        }

        currentQuestion = remaining.random()

        currentQuestion?.let { q ->
            txtQuestion.text = q.text
            btnA.text = q.answers[0]
            btnB.text = q.answers[1]
            btnC.text = q.answers[2]
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val q = currentQuestion ?: return

        if (selectedIndex == q.correctIndex) {
            Toast.makeText(this, "Dobra odpowiedź!", Toast.LENGTH_SHORT).show()
            solvedIds.add(q.id.toString())
            nextQuestion()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Zła odpowiedź")
                .setMessage("Poprawna odpowiedź:\n\n${q.answers[q.correctIndex]}")
                .setPositiveButton("Dalej") { _, _ -> nextQuestion() }
                .setCancelable(false)
                .show()
        }
    }

    private fun showFinishedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Brawo!")
            .setMessage("Ukończono wszystkie pytania lokalne.\n\nZaczynamy od nowa?")
            .setPositiveButton("TAK") { _, _ ->
                solvedIds.clear()
                saveProgress()
                nextQuestion()
            }
            .setNegativeButton("NIE") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onPause() {
        super.onPause()
        saveProgress()
    }
}
