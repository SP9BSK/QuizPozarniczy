package com.example.quizpozarniczy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("QUIZ", "QuizActivity onCreate START")

        setContentView(R.layout.activity_quiz)

        val txtQuestion = findViewById<TextView>(R.id.txtQuestion)
        val txtTimer = findViewById<TextView>(R.id.txtTimer)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnB = findViewById<Button>(R.id.btnB)
        val btnC = findViewById<Button>(R.id.btnC)
        val btnD = findViewById<Button>(R.id.btnD)

        // 🔐 BEZPIECZNE ODCZYTANIE DANYCH
        val players = intent.getIntExtra("PLAYERS", 1)
        val questionsLimit = intent.getIntExtra("QUESTIONS", 1)
        val timeSeconds = intent.getIntExtra("TIME_SECONDS", 60)

        Log.d("QUIZ", "players=$players questions=$questionsLimit time=$timeSeconds")

        // 🔒 TEST – NIE MA ŻADNEJ LOGIKI KOŃCZĄCEJ QUIZ
        txtQuestion.text = "QUIZ WYSTARTOWAŁ ✅"
        txtTimer.text = "Czas: ${timeSeconds / 60} min"

        btnA.text = "Odpowiedź A"
        btnB.text = "Odpowiedź B"
        btnC.text = "Odpowiedź C"
        btnD.text = "Odpowiedź D"

        btnA.setOnClickListener { }
        btnB.setOnClickListener { }
        btnC.setOnClickListener { }
        btnD.setOnClickListener { }

        Log.d("QUIZ", "QuizActivity onCreate END")
    }
}
