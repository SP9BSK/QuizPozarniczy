package com.example.quizpozarniczy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RegulaminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regulamin)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
