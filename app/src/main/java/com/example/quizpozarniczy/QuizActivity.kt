package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Question
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private var questions: List<Question> = emptyList()
    private var currentIndex = 0
    private var timer: CountDownTimer? = null
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        
        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        val questionsLimit = intent.getIntExtra("QUESTIONS", 1)
        val timeSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions
            .shuffled()
            .take(min(questionsLimit, allQuestions.size))

        startTimer(timeSeconds)
        showQuestion()

         btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnD.setOnClickListener { answerSelected(3) }


        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val min = ms / 1000 / 60
                val sec = (ms / 1000) % 60
                txtTimer.text = String.format("Czas: %02d:%02d", min, sec)
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

    private fun nextQuestion() {
        currentIndex++
        showQuestion()
    }
   
     package com.example.quizpozarniczy

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.model.Question
import kotlin.math.min

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button
    private lateinit var btnBack: Button

    private var questions: List<Question> = emptyList()
    private var currentIndex = 0
    private var timer: CountDownTimer? = null
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        
        txtQuestion = findViewById(R.id.txtQuestion)
        txtTimer = findViewById(R.id.txtTimer)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)
        btnBack = findViewById(R.id.btnBack)

        val questionsLimit = intent.getIntExtra("QUESTIONS", 1)
        val timeSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        val allQuestions = QuizRepository.getQuestions()
        questions = allQuestions
            .shuffled()
            .take(min(questionsLimit, allQuestions.size))

        startTimer(timeSeconds)
        showQuestion()

         btnA.setOnClickListener { answerSelected(0) }
        btnB.setOnClickListener { answerSelected(1) }
        btnC.setOnClickListener { answerSelected(2) }
        btnD.setOnClickListener { answerSelected(3) }


        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val min = ms / 1000 / 60
                val sec = (ms / 1000) % 60
                txtTimer.text = String.format("Czas: %02d:%02d", min, sec)
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

    private fun nextQuestion() {
        currentIndex++
        showQuestion()
    }
   
        nextQuestion()
    }
private fun answerSelected(selectedIndex: Int) {
        val correct = questions[currentIndex].correctIndex
        if (selectedIndex == correct) {
            score++
        }
        nextQuestion()
    }
    private fun endQuiz() {
    

        timer?.cancel()

               txtQuestion.text = "Koniec quizu\n\nWynik: $score / ${questions.size}"

        txtTimer.text = ""

        btnA.visibility = View.GONE
        btnB.visibility = View.GONE
        btnC.visibility = View.GONE
        btnD.visibility = View.GONE

        btnBack.visibility = View.VISIBLE
    }
}
