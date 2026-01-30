package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class EditLocalQuestionsActivity : AppCompatActivity() {

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val etA = findViewById<EditText>(R.id.etA)
        val etB = findViewById<EditText>(R.id.etB)
        val etC = findViewById<EditText>(R.id.etC)
        val rgCorrect = findViewById<RadioGroup>(R.id.rgCorrect)

        val btnPrev = findViewById<Button>(R.id.btnPrev)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnSave = findViewById<Button>(R.id.btnSave)

        fun loadQuestion() {
            val q = LocalQuestionsRepository.questions[currentIndex]
            txtQuestion.text = q.question
            etA.setText(q.answers[0])
            etB.setText(q.answers[1])
            etC.setText(q.answers[2])
            rgCorrect.check(
                when (q.correctIndex) {
                    0 -> R.id.rbA
                    1 -> R.id.rbB
                    else -> R.id.rbC
                }
            )
        }

        fun saveQuestion() {
            val q = LocalQuestionsRepository.questions[currentIndex]
            q.answers[0] = etA.text.toString()
            q.answers[1] = etB.text.toString()
            q.answers[2] = etC.text.toString()

            q.correctIndex = when (rgCorrect.checkedRadioButtonId) {
                R.id.rbA -> 0
                R.id.rbB -> 1
                else -> 2
            }
        }

        btnPrev.setOnClickListener {
            saveQuestion()
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion()
            }
        }

        btnNext.setOnClickListener {
            saveQuestion()
            if (currentIndex < LocalQuestionsRepository.questions.size - 1) {
                currentIndex++
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
