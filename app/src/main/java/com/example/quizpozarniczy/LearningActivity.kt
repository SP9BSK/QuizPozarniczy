package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.Question
import kotlin.random.Random

class LearningActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button

    private val allQuestions = mutableListOf<Question>()
    private val remainingQuestions = mutableListOf<Question>()

    private lateinit var currentQuestion: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)

        loadAllQuestions()
        startLearning()

        btnA.setOnClickListener { checkAnswer(0) }
        btnB.setOnClickListener { checkAnswer(1) }
        btnC.setOnClickListener { checkAnswer(2) }
    }

    private fun loadAllQuestions() {
        // pytania ogólne
        allQuestions.addAll(QuizRepository.getQuestions())

        // pytania lokalne – BEZ CUDZYSŁOWÓW
        allQuestions.addAll(
            LocalQuestionsRepository.toQuizQuestions(Int.MAX_VALUE)
        )
    }

    private fun startLearning() {
        remainingQuestions.clear()
        remainingQuestions.addAll(allQuestions)
        nextQuestion()
    }

    private fun nextQuestion() {
        if (remainingQuestions.isEmpty()) {
            showFinishedDialog()
            return
        }

        currentQuestion = remainingQuestions.random()

        txtQuestion.text = currentQuestion.text
        btnA.text = currentQuestion.answers[0]
        btnB.text = currentQuestion.answers[1]
        btnC.text = currentQuestion.answers[2]
    }

    private fun checkAnswer(selectedIndex: Int) {
        if (selectedIndex == currentQuestion.correctIndex) {
            Toast.makeText(this, "✅ Dobra odpowiedź!", Toast.LENGTH_SHORT).show()
            remainingQuestions.remove(currentQuestion)
            nextQuestion()
        } else {
            val correctText = currentQuestion.answers[currentQuestion.correctIndex]
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

    private fun showFinishedDialog() {
        AlertDialog.Builder(this)
            .setTitle("🎉 Brawo!")
            .setMessage(
                "Na wszystkie pytania zostały udzielone poprawne odpowiedzi.\n\nCzy zaczynamy od początku?"
            )
            .setPositiveButton("TAK") { _, _ ->
                startLearning()
            }
            .setNegativeButton("NIE") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
