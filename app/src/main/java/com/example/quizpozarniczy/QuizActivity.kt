package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnNext: Button

    private lateinit var questions: List<Question>
    private var currentIndex = 0

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnNext = findViewById(R.id.btnNext)

        // ====== DANE Z PANELU SĘDZIEGO ======
        val players = intent.getIntExtra("PLAYERS", 1)
        val questionLimit = intent.getIntExtra("QUESTIONS", 10)
        val timeMinutes = intent.getIntExtra("TIME", 10)

        val totalTimeMillis = timeMinutes * 60 * 1000L

        // ====== PYTANIA ======
        questions = QuizRepository.getQuestions().take(questionLimit)

        showQuestion()

        // ====== TIMER ======
        timer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                txtTimer.text = "$minutes:${seconds.toString().padStart(2, '0')}"
            }

            override fun onFinish() {
                txtTimer.text = "KONIEC CZASU"
                btnNext.isEnabled = false
            }
        }.start()

        // ====== NASTĘPNE PYTANIE ======
        btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                showQuestion()
            } else {
                txtQuestion.text = "Koniec quizu"
                btnNext.isEnabled = false
                timer?.cancel()
            }
        }
    }

    private fun showQuestion() {
        txtQuestion.text = questions[currentIndex].text
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
