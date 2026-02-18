package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirstLaunchActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Jeśli już zapisane → przejdź dalej
if (prefs.getBoolean("dane_zapisane", false)) {
    startActivity(Intent(this, StartActivity::class.java))
    finish()
    return
}

        setContentView(R.layout.activity_first_launch)

        val editWoj = findViewById<EditText>(R.id.editWojewodztwo)
        val editPow = findViewById<EditText>(R.id.editPowiat)
        val editJed = findViewById<EditText>(R.id.editJednostka)
        val btn = findViewById<Button>(R.id.buttonZapisz)

        btn.setOnClickListener {

            val woj = editWoj.text.toString().trim()
            val pow = editPow.text.toString().trim()
            val jed = editJed.text.toString().trim()

            if (woj.isEmpty() || pow.isEmpty()) {
                return@setOnClickListener
            }

            val data = hashMapOf(
                "wojewodztwo" to woj,
                "powiat" to pow,
                "jednostka" to jed,
                "typ" to BuildConfig.FLAVOR, // opiekun / mlodziez
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("statystyki")
    .add(data)
    .addOnSuccessListener {

        prefs.edit().putBoolean("dane_zapisane", true).apply()

        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
        }
    }
}
