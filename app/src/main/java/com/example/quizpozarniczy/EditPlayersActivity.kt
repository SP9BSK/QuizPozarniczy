package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class EditPlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_players)

        val container = findViewById<LinearLayout>(R.id.playersContainer)
        val btnSave = findViewById<Button>(R.id.btnSavePlayers)

        container.removeAllViews()

        // Wczytaj aktualne nazwiska zawodników
        QuizSession.playerNames.forEachIndexed { index, name ->
            val et = EditText(this)
            et.hint = "Zawodnik ${index + 1}"
            et.setText(name)
            et.textSize = 18f
            container.addView(et)
        }

        btnSave.setOnClickListener {
            QuizSession.playerNames.clear()
            for (i in 0 until container.childCount) {
                val et = container.getChildAt(i) as EditText
                val text = et.text.toString().ifBlank { "Zawodnik ${i + 1}" }
                QuizSession.playerNames.add(text)
            }

            // Po zapisaniu nazw od razu startujemy QuizActivity
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
