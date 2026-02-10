package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.data.LocalQuestionsRepository

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnExportImport = findViewById<Button>(R.id.btnExportImport)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // 🔹 Inicjalizacja pytań lokalnych
        if (isOpiekun) {
            LocalQuestionsRepository.questions.clear()
            LocalQuestionsRepository.questions.addAll(DefaultLocalQuestions.questions)
        } else {
            LocalQuestionsRepository.questions.clear()
        }

        // 1️⃣ EDYCJA / B
        if (isOpiekun) {
            btnEditOrB.text = "EDYCJA PYTAŃ LOKALNYCH"
            btnEditOrB.setOnClickListener {
                startActivity(Intent(this, EditLocalQuestionsActivity::class.java))
            }
        } else {
            btnEditOrB.text = "B"
            btnEditOrB.setOnClickListener {
                Toast.makeText(this, "Funkcja B (do implementacji)", Toast.LENGTH_SHORT).show()
            }
        }

        // 2️⃣ A – tymczasowo nieaktywne
        btnA.text = "A – do późniejszego wykorzystania"
        btnA.setOnClickListener {
            Toast.makeText(this, "Funkcja A będzie dostępna w późniejszej wersji", Toast.LENGTH_SHORT).show()
        }

        // 3️⃣ EXPORT / IMPORT – tymczasowo wyłączone, żeby build był stabilny
        btnExportImport.setOnClickListener {
            Toast.makeText(this, "Eksport / import – wkrótce", Toast.LENGTH_SHORT).show()
        }

        // 4️⃣ REGULAMIN
        btnRegulamin.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }
}
