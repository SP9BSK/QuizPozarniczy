package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var txtResult: TextView
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button
    private lateinit var btnBack: Button

    private var index = 0
    private var score = 0
    private lateinit var questions: List<String>
    private lateinit var answers: List<Boolean>
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        txtResult = findViewById(R.id.txtResult)
        btnYes = findViewById(R.id.btnYes)
        btnNo = findViewById(R.id.btnNo)
        btnBack = findViewById(R.id.btnBack)

        val questionsLimit = intent.getIntExtra("QUESTIONS_COUNT", 5)
        val quizTime = intent.getIntExtra("QUIZ_TIME", 10)

        val allQuestions = listOf(
            "Czy woda nadaje się do gaszenia oleju?",
            "Czy strażak musi nosić hełm?",
            "Czy CO2 przewodzi prąd?",
            "Czy dym jest niebezpieczny?",
            "Czy gaśnica proszkowa jest uniwersalna?"
        )

        val allAnswers = listOf(false, true, false, true, true)

        questions = allQuestions.take(questionsLimit)
        answers = allAnswers.take(questionsLimit)

        showQuestion()
        startTimer(quizTime)

        btnYes.setOnClickListener { answer(true) }
        btnNo.setOnClickListener { answer(false) }

        btnBack.setOnClickListener {
            startActivity(Intent(this, JudgeActivity::class.java))
            finish()
        }
    }

    private fun showQuestion() {
        if (index < questions.size) {
            txtQuestion.text = questions[index]
        } else {
            endQuiz()
        }
    }

    private fun answer(userAnswer: Boolean) {
        if (userAnswer == answers[index]) {
            score++
        }
        index++
        showQuestion()
    }

    private fun startTimer(minutes: Int) {
        timer = object : CountDownTimer(minutes * 60 * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val min = ms / 60000
                val sec = (ms % 60000) / 1000
                txtTimer.text = "Czas: %02d:%02d".format(min, sec)
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        timer?.cancel()
        txtQuestion.text = "Koniec quizu"
        txtResult.text = "Wynik: $score / ${questions.size}"
        btnYes.isEnabled = false
        btnNo.isEnabled = false
        btnBack.visibility = Button.VISIBLE
    }
}
