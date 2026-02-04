package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.Question

class LearningActivity : AppCompatActivity() {

    private lateinit var txtProgress: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnSaveExit: Button

    private val prefsName = "learning_mode"
    private val keySolved = "solved_ids"

    private val allQuestions = mutableListOf<Question>()
    private val solvedIds = mutableSetOf<String>()
    private lateinit var currentQuestion: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        txtProgress = findViewById(R.id.txtProgress)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnSaveExit = findViewById(R.id.btnSaveExit)

        loadProgress()
        loadAllQuestions()
        nextQuestion()

        btnA.setOnClickListener { checkAnswer(0) }
        btnB.setOnClickListener { checkAnswer(1) }
        btnC.setOnClickListener { checkAnswer(2) }

        btnSaveExit.setOnClickListener {
            saveProgress()
            finish()
        }
    }

    private fun loadAllQuestions() {
        allQuestions.clear()

        // pytania ogólne
        allQuestions.addAll(QuizRepository.getQuestions())

        // pytania lokalne – BEZ CUDZYSŁOWÓW
        allQuestions.addAll(
            LocalQuestionsRepository.toQuizQuestions(Int.MAX_VALUE)
        )

        updateProgress()
    }

    private fun loadProgress() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        solvedIds.clear()
        solvedIds.addAll(prefs.getStringSet(keySolved, emptySet()) ?: emptySet())
    }

    private fun saveProgress() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        prefs.edit()
            .putStringSet(keySolved, solvedIds)
            .apply()
    }

    private fun nextQuestion() {
        updateProgress()

        val remaining = allQuestions.filter { it.text !in solvedIds }

        if (remaining.isEmpty()) {
            showFinishedDialog()
            return
        }

        currentQuestion = remaining.random()

        txtQuestion.text = currentQuestion.text
        btnA.text = currentQuestion.answers[0]
        btnB.text = currentQuestion.answers[1]
        btnC.text = currentQuestion.answers[2]
    }

    private fun checkAnswer(selectedIndex: Int) {
        if (selectedIndex == currentQuestion.correctIndex) {
            Toast.makeText(this, "✅ Dobra odpowiedź!", Toast.LENGTH_SHORT).show()
            solvedIds.add(currentQuestion.text)
            nextQuestion()
        } else {
            val correctText =
                currentQuestion.answers[currentQuestion.correctIndex]

            AlertDialog.Builder(this)
                .setTitle("❌ Zła odpowiedź")
                .setMessage("Poprawna odpowiedź to:\n\n$correctText")
                .setPositiveButton("Dalej") { _, _ ->
                    nextQuestion()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun updateProgress() {
        txtProgress.text =
            "Opanowane: ${solvedIds.size} / ${allQuestions.size}"
    }

    private fun showFinishedDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎉 Brawo!")
            .setMessage(
                "Na wszystkie pytania zostały udzielone poprawne odpowiedzi.\n\nCzy zaczynamy od początku?"
            )
            .setPositiveButton("TAK") { _, _ ->
                solvedIds.clear()
                saveProgress()
                nextQuestion()
            }
            .setNegativeButton("NIE") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onPause() {
        super.onPause()
        saveProgress()
    }
}
