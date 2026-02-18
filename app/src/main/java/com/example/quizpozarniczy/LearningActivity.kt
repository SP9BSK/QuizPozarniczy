package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.data.QuestionsPart1
import com.example.quizpozarniczy.data.QuestionsPart2
import com.example.quizpozarniczy.data.QuestionsPart3
import com.example.quizpozarniczy.data.QuestionsPart4
import com.example.quizpozarniczy.data.QuestionsPart5
import com.example.quizpozarniczy.data.QuestionsPart6
import com.example.quizpozarniczy.data.QuestionsPart7
import com.example.quizpozarniczy.data.QuestionsPart8
import com.example.quizpozarniczy.data.QuestionsPart9
import com.example.quizpozarniczy.data.QuestionsPart10
import com.example.quizpozarniczy.data.QuestionsPart11
import com.example.quizpozarniczy.data.QuestionsPart12
import com.example.quizpozarniczy.data.QuestionsPart13
import com.example.quizpozarniczy.data.QuestionsPart14
import com.example.quizpozarniczy.data.QuestionsPart15
import com.example.quizpozarniczy.data.QuestionsPart16
import com.example.quizpozarniczy.data.QuestionsPart17
import com.example.quizpozarniczy.data.QuestionsPart18
import com.example.quizpozarniczy.data.QuestionsPart19
import com.example.quizpozarniczy.data.QuestionsPart20
import com.example.quizpozarniczy.data.QuestionsPart21
import com.example.quizpozarniczy.model.Question

class LearningActivity : AppCompatActivity() {

    private lateinit var txtProgress: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnSaveExit: Button

    private lateinit var learningMode: String

    private val prefsName by lazy {
        "${packageName}_learning_$learningMode"
    }

    private val keySolved = "solved_ids"

    private val allQuestions = mutableListOf<Question>()
    private val solvedIds = mutableSetOf<String>()
    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        learningMode = intent.getStringExtra("LEARNING_MODE") ?: "GENERAL"

        txtProgress = findViewById(R.id.txtProgress)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnSaveExit = findViewById(R.id.btnSaveExit)

        loadProgress()
        loadAllQuestions()

        if (allQuestions.isEmpty()) {
            Toast.makeText(
                this,
                "Brak pytań w tym trybie",
                Toast.LENGTH_LONG
            ).show()
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

    private fun loadAllQuestions() {
        allQuestions.clear()

        when (learningMode) {
            "GENERAL" -> {
                allQuestions.addAll(QuestionsPart1.get())
                allQuestions.addAll(QuestionsPart2.get())
                allQuestions.addAll(QuestionsPart3.get())
                allQuestions.addAll(QuestionsPart4.get())
                allQuestions.addAll(QuestionsPart5.get())
                allQuestions.addAll(QuestionsPart6.get())
                allQuestions.addAll(QuestionsPart7.get())
                allQuestions.addAll(QuestionsPart8.get())
                allQuestions.addAll(QuestionsPart9.get())
                allQuestions.addAll(QuestionsPart10.get())
                allQuestions.addAll(QuestionsPart11.get())
                allQuestions.addAll(QuestionsPart12.get())
                allQuestions.addAll(QuestionsPart13.get())
                allQuestions.addAll(QuestionsPart14.get())
                allQuestions.addAll(QuestionsPart15.get())
                allQuestions.addAll(QuestionsPart16.get())
                allQuestions.addAll(QuestionsPart17.get())
                allQuestions.addAll(QuestionsPart18.get())
                allQuestions.addAll(QuestionsPart19.get())
                allQuestions.addAll(QuestionsPart20.get())
                allQuestions.addAll(QuestionsPart21.get())
            }

            "LOCAL" -> {
                allQuestions.addAll(
                    LocalQuestionsRepository.toQuizQuestions(Int.MAX_VALUE)
                )
            }
        }

        updateProgress()
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
        updateProgress()

        val remaining = allQuestions.filter { it.text !in solvedIds }

        if (remaining.isEmpty()) {
            showFinishedDialog()
            return
        }

        currentQuestion = remaining.random()

        txtQuestion.text = currentQuestion!!.text
        btnA.text = currentQuestion!!.answers[0]
        btnB.text = currentQuestion!!.answers[1]
        btnC.text = currentQuestion!!.answers[2]
    }

    private fun checkAnswer(selectedIndex: Int) {
        val q = currentQuestion ?: return

        if (selectedIndex == q.correctIndex) {
            Toast.makeText(this, "✅ Dobra odpowiedź!", Toast.LENGTH_SHORT).show()
            solvedIds.add(q.text)
            nextQuestion()
        } else {
            AlertDialog.Builder(this)
                .setTitle("❌ Zła odpowiedź")
                .setMessage("Poprawna odpowiedź:\n\n${q.answers[q.correctIndex]}")
                .setPositiveButton("Dalej") { _, _ -> nextQuestion() }
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
                "Na wszystkie pytania udzielono poprawnych odpowiedzi.\n\nZaczynamy od nowa?"
            )
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
