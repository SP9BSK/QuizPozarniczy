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

        val txtPreview = findViewById<TextView>(R.id.txtPreview)
        val etQuoted = findViewById<EditText>(R.id.etQuotedValue)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val question = LocalQuestionsRepository.questions[currentIndex]

        fun refreshPreview() {
            txtPreview.text = question.fullQuestion()
        }

        etQuoted.setText(question.quotedValue)
        refreshPreview()

        etQuoted.addTextChangedListener {
            question.quotedValue = it.toString()
            refreshPreview()
        }

        btnSave.setOnClickListener {
            Toast.makeText(this, "Zapisano zmiany", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

