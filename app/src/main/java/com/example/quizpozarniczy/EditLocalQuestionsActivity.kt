package com.example.quizpozarniczy

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.LocalQuestion

class EditLocalQuestionsActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var etA: EditText
    private lateinit var etB: EditText
    private lateinit var etC: EditText
    private lateinit var rgCorrect: RadioGroup
    private lateinit var rbA: RadioButton
    private lateinit var rbB: RadioButton
    private lateinit var rbC: RadioButton
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnSave: Button

    private var currentIndex = 0
    private val questions = LocalQuestionsRepository.questions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)

        txtQuestion = findViewById(R.id.txtQuestion)
        etA = findViewById(R.id.etA)
        etB = findViewById(R.id.etB)
        etC = findViewById(R.id.etC)
        rgCorrect = findViewById(R.id.rgCorrect)
        rbA = findViewById(R.id.rbA)
        rbB = findViewById(R.id.rbB)
        rbC = findViewById(R.id.rbC)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnSave = findViewById(R.id.btnSave)

        loadQuestion()

        btnPrev.setOnClickListener {
            saveCurrent()
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion()
            }
        }

        btnNext.setOnClickListener {
            saveCurrent()
            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion()
            }
        }

        btnSave.setOnClickListener {
            saveCurrent()
            finish()
        }
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]

        txtQuestion.text = q.fullQuestion()

        etA.setText(q.answers[0])
        etB.setText(q.answers[1])
        etC.setText(q.answers[2])

        when (q.correctIndex) {
            0 -> rbA.isChecked = true
            1 -> rbB.isChecked = true
            2 -> rbC.isChecked = true
        }

        // 🔒 blokada edycji pytań 1 i 2 (brak fragmentów edytowalnych)
        if (q.quotedValue1 == null && q.quotedValue2 == null) {
            txtQuestion.alpha = 0.6f
        } else {
            txtQuestion.alpha = 1f
        }
    }

    private fun saveCurrent() {
        val q: LocalQuestion = questions[currentIndex]

        q.answers[0] = etA.text.toString()
        q.answers[1] = etB.text.toString()
        q.answers[2] = etC.text.toString()

        q.correctIndex = when {
            rbA.isChecked -> 0
            rbB.isChecked -> 1
            rbC.isChecked -> 2
            else -> q.correctIndex
        }
    }
}
