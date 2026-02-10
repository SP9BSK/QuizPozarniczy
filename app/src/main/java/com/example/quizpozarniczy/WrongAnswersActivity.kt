package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WrongAnswersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrong_answers)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtContent: TextView = findViewById(R.id.txtWrongAnswers)

        val playerIndex = intent.getIntExtra("PLAYER_INDEX", 0)
        val result = QuizSession.results[playerIndex]

        if (result.wrongAnswers.isEmpty()) {
            txtContent.text = "Brak błędnych odpowiedzi 🎉"
            return
        }

        val sb = StringBuilder()

        result.wrongAnswers.forEachIndexed { index, wa ->
            sb.append("Pytanie ${index + 1}:\n")
            sb.append(wa.question).append("\n\n")

            wa.answers.forEachIndexed { i, ans ->
                when (i) {
                    wa.correctIndex -> sb.append("✔ ")
                    wa.chosenIndex -> sb.append("✘ ")
                    else -> sb.append("  ")
                }
                sb.append(ans).append("\n")
            }
            sb.append("\n---------------------\n\n")
        }

        txtContent.text = sb.toString()
    }
}
