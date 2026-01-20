package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.repository.QuizRepository

class QuizActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var txtResult: TextView
    private lateinit var btnBack: Button

    private var currentIndex = 0
    private var points = 0
    private lateinit var questions: List<Question>
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtTimer = findViewById(R.id.txtTimer)
        txtQuestion = findViewById(R.id.txtQuestion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        txtResult = findViewById(R.id.txtResult)
        btnBack = findViewById(R.id.btnBack)

        val questionLimit = intent.getIntExtra("QUESTIONS", 1)
        val timeSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        questions = QuizRepository.getQuestions().take(questionLimit)

        startTimer(timeSeconds)
        showQuestion()

        val buttons = listOf(btnA, btnB, btnC, btnD)
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { checkAnswer(index) }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startTimer(seconds: Int) {
        timer?.cancel()
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val totalSeconds = ms / 1000
                val minutes = totalSeconds / 60
                val sec = totalSeconds % 60
                txtTimer.text = "Czas: %02d:%02d".format(minutes, sec)
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()
    }

    private fun showQuestion() {
        if (currentIndex >= questions.size) {
            endQuiz()
            return
        }

        val q = questions[currentIndex]
        txtQuestion.text = q.text
        btnA.text = q.answers[0]
        btnB.text = q.answers[1]
        btnC.text = q.answers[2]
        btnD.text = q.answers[3]
    }

    private fun checkAnswer(selected: Int) {
        if (questions[currentIndex].correctIndex == selected) {
            points++
        }
        currentIndex++
        showQuestion()
    }

    private fun endQuiz() {
        timer?.cancel()

        // 🔴 czyścimy ekran pytań
        txtQuestion.visibility = View.GONE
        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        txtResult.visibility = View.VISIBLE
        btnBack.visibility = View.VISIBLE
        txtResult.text = "Koniec quizu\nWynik: $points / ${questions.size}"
    }
}
