package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        // Jeśli dane już zapisane → przejdź dalej
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

        // Ustaw tryb aplikacji
        tvMode.text = if (BuildConfig.FLAVOR == "full") "Opiekun" else "Młodzież"

        // Przyciski początkowo nieaktywne
        btnZapisz.isEnabled = false

        // Województwa
        val wojewodztwa = resources.getStringArray(R.array.wojewodztwa)
        spinnerWoj.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            wojewodztwa
        )

        // Zmiana województwa → zmiana powiatów
        spinnerWoj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                val powiatyId = when (position) {
                    0 -> R.array.powiaty_dolnoslaskie
                    1 -> R.array.powiaty_kujawsko_pomorskie
                    2 -> R.array.powiaty_lubelskie
                    3 -> R.array.powiaty_lubuskie
                    4 -> R.array.powiaty_lodzkie
                    5 -> R.array.powiaty_malopolskie
                    6 -> R.array.powiaty_mazowieckie
                    7 -> R.array.powiaty_opolskie
                    8 -> R.array.powiaty_podkarpackie
                    9 -> R.array.powiaty_podlaskie
                    10 -> R.array.powiaty_pomorskie
                    11 -> R.array.powiaty_slaskie
                    12 -> R.array.powiaty_swietokrzyskie
                    13 -> R.array.powiaty_warminsko_mazurskie
                    14 -> R.array.powiaty_wielkopolskie
                    else -> R.array.powiaty_zachodniopomorskie
                }

                val powiaty = resources.getStringArray(powiatyId)

                spinnerPow.adapter = ArrayAdapter(
                    this@FirstLaunchActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    powiaty
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Aktywacja przycisku po zaznaczeniu regulaminu
        checkReg.setOnCheckedChangeListener { _, isChecked ->
            btnZapisz.isEnabled = isChecked
        }

        // Otwórz regulamin
        btnReg.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }

        // Zapis danych
        btnZapisz.setOnClickListener {

            val woj = spinnerWoj.selectedItem?.toString() ?: ""
            val pow = spinnerPow.selectedItem?.toString() ?: ""

            if (woj.isEmpty() || pow.isEmpty()) {
                Toast.makeText(this, "Wybierz województwo i powiat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = hashMapOf(
                "wojewodztwo" to woj,
                "powiat" to pow,
                "jednostka" to editJed.text.toString(),
                "typ" to BuildConfig.FLAVOR,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("statystyki")
                .add(data)
                .addOnSuccessListener {

                    prefs.edit()
                        .putBoolean("dane_zapisane", true)
                        .apply()

                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Błąd zapisu danych", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
