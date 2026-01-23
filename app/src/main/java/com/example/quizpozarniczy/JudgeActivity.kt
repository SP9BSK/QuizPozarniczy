package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JudgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_judge)

        val etPlayers = findViewById<EditText>(R.id.etPlayers)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {

            val players = validateAndFix(
                etPlayers,
                min = 1,
                max = 10,
                label = "Liczba zawodników"
            )

            val questions = validateAndFix(
                etQuestions,
                min = 1,
                max = 100,
                label = "Liczba pytań"
            )

            val minutes = validateAndFix(
                etTime,
                min = 1,
                max = 30,
                label = "Czas (minuty)"
            )

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYERS", players)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("TIME_SECONDS", minutes * 60)

            startActivity(intent)
        }
    }

    // ================= WALIDACJA =================

    private fun validateAndFix(
        editText: EditText,
        min: Int,
        max: Int,
        label: String
    ): Int {
        val value = editText.text.toString().toIntOrNull()

        return when {
            value == null || value < min -> {
                Toast.makeText(
                    this,
                    "$label nie może być mniejsze niż $min",
                    Toast.LENGTH_SHORT
                ).show()
                editText.setText(min.toString())
                min
            }

            value > max -> {
                Toast.makeText(
                    this,
