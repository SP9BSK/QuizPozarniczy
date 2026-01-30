package com.example.quizpozarniczy

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.LocalQuestion

class EditLocalQuestionsActivity : AppCompatActivity() {

    private var index = 0
    private lateinit var question: LocalQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val etQuoted = findViewById<EditText>(R.id.etQuoted)

        val etA = findViewById<EditText>(R.id.etA)
        val etB = findViewById<EditText>(R.id.etB)
        val etC = findViewById<EditText>(R.id.etC)

        val rgCorrect = findViewById<RadioGroup>(R.id.rgCorrect)
        val rbA = findViewById<RadioButton>(R.id.rbA)
        val rbB = findViewById<RadioButton>(R.id.rbB)
        val rbC = findViewById<RadioButton>(R.id.rbC)

        val btnPrev = findViewById<Button>(R.id.btnPrev)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnSave = findViewById<Button>(R.id.btnSave)

        fun loadQuestion() {
            question = LocalQuestionsRepository.questions[index]

            // 🔹 pytanie bez edycji
            txtQuestion.text = question.fullQuestion()

            // 🔥 część w cudzysłowie TYLKO jeśli istnieje
            if (question.quotedValue != null) {
                etQuoted.visibility = View.VISIBLE
                etQuoted.setText(question.quotedValue)
            } else {
                etQuoted.visibility = View.GONE
            }

            etA.setText(question.answers[0])
            etB.setText(question.answers[1])
            etC.setText(question.answers[2])

            rgCorrect.clearCheck()
            when (question.correctIndex) {
                0 -> rbA.isChecked = true
                1 -> rbB.isChecked = true
                2 -> rbC.isChecked = true
            }
        }

        fun saveQuestion() {
            question.answers[0] = etA.text.toString()
            question.answers[1] = etB.text.toString()
            question.answers[2] = etC.text.toString()

            if (question.quotedValue != null) {
                question.quotedValue = etQuoted.text.toString()
            }

            question.correctIndex = when (rgCorrect.checkedRadioButtonId) {
                R.id.rbA -> 0
                R.id.rbB -> 1
                R.id.rbC -> 2
                else -> 0
            }
        }

        btnPrev.setOnClickListener {
            saveQuestion()
            if (index > 0) {
                index--
                loadQuestion()
            }
        }

        btnNext.setOnClickListener {
            saveQuestion()
            if (index < LocalQuestionsRepository.questions.lastIndex) {
                index++
                loadQuestion()
            }
        }

        btnSave.setOnClickListener {
            saveQuestion()
            Toast.makeText(this, "Zapisano pytania lokalne", Toast.LENGTH_SHORT).show()
            finish()
        }

        loadQuestion()
    }
}
