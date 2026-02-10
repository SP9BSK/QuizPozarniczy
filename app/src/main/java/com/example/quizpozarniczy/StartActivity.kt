package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizImporter

class StartActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // 🔹 inicjalizacja repozytorium
        LocalQuestionsRepository.init(
            context = this,
            loadDefaults = isOpiekun
        )

        // 🔹 PRZYCISK A / PANEL SĘDZIEGO
        findViewById<Button>(R.id.btnJudge).setOnClickListener {
            if (isOpiekun) {
                startActivity(
                    Intent(this, JudgeActivity::class.java)
                )
            } else {
                importSinglePlayerQuiz()
            }
        }

        // 🔹 TRYB NAUKI
        findViewById<Button>(R.id.btnLearn).setOnClickListener {
            startActivity(
                Intent(this, LearningModeSelectActivity::class.java)
            )
        }

        // 🔹 USTAWIENIA
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }
    }

    private fun importSinglePlayerQuiz() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return

            contentResolver.openInputStream(uri)?.use { inputStream ->
                val quiz = QuizImporter.importSinglePlayerQuiz(inputStream)

                if (quiz != null) {
                    // ❗ NIE MA setQuestions – pracujemy na liście
                    QuizSession.playerNames.clear()
                    QuizSession.playerNames.add(quiz.playerName)

                    QuizSession.questions.clear()
                    QuizSession.questions.addAll(quiz.questions)

                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra("TIME_SECONDS", quiz.timeSeconds)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Nie udało się wczytać quizu",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
