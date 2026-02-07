package com.example.quizpozarniczy

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.LocalQuestion

class EditLocalQuestionsActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var etQuoted1: EditText
    private lateinit var etQuoted2: EditText
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

        // -------------------------------
        // Zabezpieczenie: brak pytań
        // -------------------------------
        if (questions.isEmpty()) {
            Toast.makeText(
                this,
                "Brak pytań lokalnych do edycji",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        txtQuestion = findViewById(R.id.txtQuestion)
        etQuoted1 = findViewById(R.id.etQuoted1)
        etQuoted2 = findViewById(R.id.etQuoted2)
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
            LocalQuestionsRepository.save(this)
            finish()
        }
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]

        txtQuestion.text = q.fullQuestion()

        if (q.quotedValue1 != null) {
            etQuoted1.visibility = View.VISIBLE
            etQuoted1.setText(q.quotedValue1)
        } else {
            etQuoted1.visibility = View.GONE
        }

        if (q.quotedValue2 != null) {
            etQuoted2.visibility = View.VISIBLE
            etQuoted2.setText(q.quotedValue2)
        } else {
            etQuoted2.visibility = View.GONE
        }

        etA.setText(q.answers[0])
        etB.setText(q.answers[1])
        etC.setText(q.answers[2])

        when (q.correctIndex) {
            0 -> rbA.isChecked = true
            1 -> rbB.isChecked = true
            2 -> rbC.isChecked = true
        }
    }

    private fun saveCurrent() {
        val q = questions[currentIndex]

        if (q.quotedValue1 != null) {
            q.quotedValue1 = etQuoted1.text.toString()
        }

        if (q.quotedValue2 != null) {
            q.quotedValue2 = etQuoted2.text.toString()
        }

        q.answers[0] = etA.text.toString()
        q.answers[1] = etB.text.toString()
        q.answers[2] = etC.text.toString()

        q.correctIndex = when {
            rbA.isChecked -> 0
            rbB.isChecked -> 1
            rbC.isChecked -> 2
            else -> q.correctIndex
        }

        txtQuestion.text = q.fullQuestion()
    }
}
