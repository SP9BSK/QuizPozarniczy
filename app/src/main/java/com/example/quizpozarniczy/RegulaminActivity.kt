package com.example.quizpozarniczy

import android.os.Bundle
import android.view.WindowManager   // ⬅️ TO BYŁO POTRZEBNE
import androidx.appcompat.app.AppCompatActivity

class RegulaminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regulamin)

        // 🔥 NIE GAŚ EKRAN
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
