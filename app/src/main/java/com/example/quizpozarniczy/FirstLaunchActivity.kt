package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirstLaunchActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        if (prefs.getBoolean("dane_zapisane", false)) {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
            return
        }

        val tvMode = findViewById<TextView>(R.id.tvMode)
        val spinnerWoj = findViewById<Spinner>(R.id.spinnerWoj)
        val spinnerPow = findViewById<Spinner>(R.id.spinnerPow)
        val editJed = findViewById<EditText>(R.id.editJednostka)
        val checkReg = findViewById<CheckBox>(R.id.checkRegulamin)
        val btnReg = findViewById<Button>(R.id.btnRegulamin)
        val btnZapisz = findViewById<Button>(R.id.btnZapisz)

        tvMode.text = if (BuildConfig.FLAVOR == "full") "Opiekun" else "Młodzież"

        val wojewodztwa = resources.getStringArray(R.array.wojewodztwa)
        spinnerWoj.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, wojewodztwa)

        spinnerWoj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val powiatyId = resources.getIdentifier(
                    "powiaty_${wojewodztwa[position]}",
                    "array",
                    packageName
                )
                if (powiatyId != 0) {
                    val powiaty = resources.getStringArray(powiatyId)
                    spinnerPow.adapter = ArrayAdapter(this@FirstLaunchActivity, android.R.layout.simple_spinner_dropdown_item, powiaty)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        checkReg.setOnCheckedChangeListener { _, isChecked ->
            btnZapisz.isEnabled = isChecked
        }

        btnReg.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }

        btnZapisz.setOnClickListener {

            val data = hashMapOf(
                "wojewodztwo" to spinnerWoj.selectedItem.toString(),
                "powiat" to spinnerPow.selectedItem.toString(),
                "jednostka" to editJed.text.toString(),
                "typ" to BuildConfig.FLAVOR,
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
