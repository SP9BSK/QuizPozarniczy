package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class EditLocalQuestionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 🔹 Na razie edytujemy PIERWSZE pytanie (jak wcześniej)
        val question = LocalQuestionsRepository.questions.first()

        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val etQuoted = findViewById<EditText>(R.id.etQuoted)
        val etA = findViewById<EditText>(R.id.etA)
        val etB = findViewById<EditText>(R.id.etB)
        val etC = findViewById<EditText>(R.id.etC)
        val spCorrect = findViewById<Spinner>(R.id.spCorrect)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // 🔹 Wyświetlenie pytania
        txtQuestion.text = question.fullQuestion()
        etQuoted.setText(question.quotedValue)

        etA.setText(question.answers[0])
        etB.setText(question.answers[1])
        etC.setText(question.answers[2])

        spCorrect.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("A", "B", "C")
        )
        spCorrect.setSelection(question.correctIndex)

        btnSave.setOnClickListener {
            question.quotedValue = etQuoted.text.toString()
            question.answers[0] = etA.text.toString()
            question.answers[1] = etB.text.toString()
            question.answers[2] = etC.text.toString()
            question.correctIndex = spCorrect.selectedItemPosition

            Toast.makeText(this, "Zapisano pytanie lokalne", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
