package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditLocalQuestionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_local_questions)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            Toast.makeText(this, "Zapisano odpowiedzi (tymczasowo)", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
