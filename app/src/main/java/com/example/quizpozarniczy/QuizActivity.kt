package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private val allQuestions = listOf(
        "Czy woda przewodzi prąd?",
        "Czy gaśnica proszkowa nadaje się do gaszenia instalacji elektrycznych?",
        "Czy tlen podtrzymuje spalanie?",
        "Czy dym jest zawsze gorący?",
        "Czy można gasić olej wodą?"
    )

    private var currentIndex = 0
    private lateinit var questions: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val btnYes = findViewById<Button>(R.id.btnYes)
        val btnNo = findViewById<Button>(R.id.btnNo)
        val txtResult = findViewById<TextView>(R.id.txtResult)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val questionsCount = intent.getIntExtra("QUESTIONS_COUNT", 1)

        questions = allQuestions.take(questionsCount.coerceAtMost(allQuestions.size))

        fun showQuestion() {
            if (currentIndex < questions.size) {
                txtQuestion.text = questions[currentIndex]
            } else {
                txtQuestion.text = "Koniec quizu"
                btnYes.isEnabled = false
                btnNo.isEnabled = false
                btnBack.visibility = Button.VISIBLE
            }
        }

        btnYes.setOnClickListener {
            currentIndex++
            showQuestion()
        }

        btnNo.setOnClickListener {
            currentIndex++
            showQuestion()
        }

        btnBack.setOnClickListener {
            finish()
        }

        showQuestion()
    }
}
