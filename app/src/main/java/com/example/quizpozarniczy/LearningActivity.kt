package com.example.quizpozarniczy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    private var allQuestions: List<Question> = emptyList()
    private val solvedIds = mutableSetOf<String>()
    private var currentQuestion: Question? = null

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
        loadQuestions()
        showNextQuestion()

        btnA.setOnClickListener { answerClicked(0) }
        btnB.setOnClickListener { answerClicked(1) }
        btnC.setOnClickListener { answerClicked(2) }

        btnSaveExit.setOnClickListener {
            saveProgress()
            finish()
        }
    }

    private fun loadQuestions() {
        val local = LocalQuestionsRepository.toQuizQuestions(Int.MAX_VALUE)
        val normal = QuizRepository.getQuestions()

        allQuestions = (local + normal)
            .distinctBy { it.text } // zabezpieczenie przed duplikatami
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

    private fun showNextQuestion() {
        updateProgress()

        val remaining = allQuestions.filter { it.text !in solvedIds }

        if (remaining.isEmpty()) {
            txtQuestion.text =
                "🎉 Wszystkie pytania opanowane!\n\nCzy chcesz zacząć od początku?"
            btnA.text = "Tak"
            btnB.text = "Nie"
            btnC.visibility = View.GONE

            btnA.setOnClickListener {
                solvedIds.clear()
                saveProgress()
                recreate()
            }

            btnB.setOnClickListener {
                finish()
            }
            return
        }

        currentQuestion = remaining.random()
        val q = currentQuestion!!

        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnC.visibility = View.VISIBLE
    }

    private fun answerClicked(index: Int) {
        val q = currentQuestion ?: return

        if (index == q.correctIndex) {
            solvedIds.add(q.text)
            Toast.makeText(this, "✅ Dobra odpowiedź", Toast.LENGTH_SHORT).show()
            showNextQuestion()
        } else {
            val correct = q.answers[q.correctIndex]
            Toast.makeText(
                this,
                "❌ Zła odpowiedź\nPoprawna: $correct",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateProgress() {
        txtProgress.text =
            "Opanowane: ${solvedIds.size} / ${allQuestions.size}"
    }

    override fun onPause() {
        super.onPause()
        saveProgress()
    }
}
