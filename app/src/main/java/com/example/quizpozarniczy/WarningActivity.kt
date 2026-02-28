package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WarningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val tvWarning = findViewById<TextView>(R.id.tvWarning)
        val btnNext = findViewById<Button>(R.id.btnNext)

        tvWarning.text = "UWAGA!\nZapoznaj się z instrukcją i edytuj pytania lokalne."

        btnNext.setOnClickListener {
            prefs.edit()
                .putBoolean("warning_shown", true)
                .apply()

            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}
