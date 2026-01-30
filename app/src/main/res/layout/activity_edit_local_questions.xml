package com.example.quizpozarniczy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        // ustawienie początkowe
        etQuoted.setText(question.quotedValue)
        refreshPreview()

        // ✅ KLASYCZNY TextWatcher – PEWNY I STABILNY
        etQuoted.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                question.quotedValue = s?.toString() ?: ""
                refreshPreview()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // ✅ PRZYCISK ZAPISU – jak wcześniej
        btnSave.setOnClickListener {
            Toast.makeText(this, "Zapisano zmiany", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
