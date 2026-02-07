package com.example.quizpozarniczy

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnEditLocalQuestions =
            findViewById<Button>(R.id.btnEditLocalQuestions)
        val btnExport =
            findViewById<Button>(R.id.btnExport)
        val btnImport =
            findViewById<Button>(R.id.btnImport)
        val btnRegulamin =
            findViewById<Button>(R.id.btnRegulamin)

        val isLearning =
            BuildConfig.APPLICATION_ID.contains("mlodziez")

        if (isLearning) {
            // MŁODZIEŻ
            btnEditLocalQuestions.text = "B"
            btnExport.visibility = View.GONE
            btnImport.visibility = View.VISIBLE
        } else {
            // OPIEKUN
            btnEditLocalQuestions.text = "EDYCJA PYTAŃ LOKALNYCH"
            btnExport.visibility = View.VISIBLE
            btnImport.visibility = View.GONE
        }

        // TODO: akcje kliknięć dodasz potem
    }
}
