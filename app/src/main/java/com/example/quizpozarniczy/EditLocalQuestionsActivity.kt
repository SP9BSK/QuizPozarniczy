package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class EditLocalQuestionsActivity : AppCompatActivity() {

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val etA = findViewById<EditText>(R.id.etA)
        val etB = findViewById<EditText>(R.id.etB)
        val etC = findViewById<EditText>(R.id.etC)

        val rbA = findViewById<RadioButton>(R.id.rbA)
        val rbB = findViewById<RadioButton>(R.id.rbB)
        val rbC = findViewById<RadioButton>(R.id.rbC)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnNext = findViewById<Button>(R.id.btnNext)

        fun loadQuestion() {
            val q = LocalQuestionsRepository.questions[index]

            etQuestion.setText(q.question)
            etA.setText(q.answers[0])
            etB.setText(q.answers[1])
            etC.setText(q.answers[2])

            rbA.isChecked = q.correctIndex == 0
            rbB.isChecked = q.correctIndex == 1
            rbC.isChecked = q.correctIndex == 2
        }

        fun saveQuestion() {
            val q = LocalQuestionsRepository.questions[index]
            q.question = etQuestion.text.toString()
            q.answers[0] = etA.text.toString()
            q.answers[1] = etB.text.toString()
            q.answers[2] = etC.text.toString()

            q.correctIndex = when {
                rbA.isChecked -> 0
                rbB.isChecked -> 1
                else -> 2
            }
        }

        btnSave.setOnClickListener {
            saveQuestion()
            Toast.makeText(this, "Zapisano", Toast.LENGTH_SHORT).show()
        }

        btnNext.setOnClickListener {
            saveQuestion()
            if (index < LocalQuestionsRepository.questions.lastIndex) {
                index++
                loadQuestion()
            } else {
                Toast.makeText(this, "To było ostatnie pytanie", Toast.LENGTH_SHORT).show()
            }
        }

        loadQuestion()
    }
}
