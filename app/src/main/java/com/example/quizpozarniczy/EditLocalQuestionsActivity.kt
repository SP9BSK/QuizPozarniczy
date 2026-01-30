package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.model.LocalQuestion

class EditLocalQuestionsActivity : AppCompatActivity() {

    private var index = 0
    private lateinit var currentQuestion: LocalQuestion

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
            currentQuestion = LocalQuestionsRepository.questions[index]

            etQuestion.setText(currentQuestion.question)
            etA.setText(currentQuestion.answers[0])
            etB.setText(currentQuestion.answers[1])
            etC.setText(currentQuestion.answers[2])

            rbA.isChecked = currentQuestion.correctIndex == 0
            rbB.isChecked = currentQuestion.correctIndex == 1
            rbC.isChecked = currentQuestion.correctIndex == 2
        }

        fun saveQuestion() {
            currentQuestion.question = etQuestion.text.toString()
            currentQuestion.answers[0] = etA.text.toString()
            currentQuestion.answers[1] = etB.text.toString()
            currentQuestion.answers[2] = etC.text.toString()

            currentQuestion.correctIndex = when {
                rbA.isChecked -> 0
                rbB.isChecked -> 1
                else -> 2
            }

            Toast.makeText(this, "Zapisano pytanie ${currentQuestion.id}", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            saveQuestion()
        }

        btnNext.setOnClickListener {
            saveQuestion()
            index++

            if (index < LocalQuestionsRepository.questions.size) {
                loadQuestion()
            } else {
                Toast.makeText(this, "Koniec listy pytań", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        loadQuestion()
    }
}
