package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class EditPlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_players)

        // Nie gasimy ekranu podczas edycji zawodników
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val container = findViewById<LinearLayout>(R.id.playersContainer)
        val btnSave = findViewById<Button>(R.id.btnSavePlayers)

        container.removeAllViews()

        // Tworzymy pola dla istniejących graczy
        QuizSession.playerNames.forEachIndexed { index, name ->
            val et = EditText(this)
            et.hint = "Zawodnik ${index + 1}"
            et.setText(name)
            et.textSize = 18f
            container.addView(et)
        }

        btnSave.setOnClickListener {
            // Zapisz wpisane nazwy do QuizSession
            QuizSession.playerNames.clear()
            for (i in 0 until container.childCount) {
                val et = container.getChildAt(i) as EditText
                val text = et.text.toString().ifBlank { "Zawodnik ${i + 1}" }
                QuizSession.playerNames.add(text)
            }

            // Ustaw wynik dla JudgeActivity i zamknij aktywność
            setResult(RESULT_OK)
            finish()
        }
    }
}
