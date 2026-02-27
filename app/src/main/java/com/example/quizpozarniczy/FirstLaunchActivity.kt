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

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Jeśli dane już zapisane → sprawdź czy warning był wyświetlony
        if (prefs.getBoolean("dane_zapisane", false)) {
            if (!prefs.getBoolean("warning_shown", false)) {
                startActivity(Intent(this, WarningActivity::class.java))
            } else {
                startActivity(Intent(this, StartActivity::class.java))
            }
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

        btnZapisz.isEnabled = false

        val wojewodztwa = resources.getStringArray(R.array.wojewodztwa)
        spinnerWoj.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            wojewodztwa
        )

        spinnerWoj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val powiatyId = when (position) {
                    0 -> R.array.powiaty_wybierz
                    1 -> R.array.powiaty_dolnoslaskie
                    2 -> R.array.powiaty_kujawsko_pomorskie
                    3 -> R.array.powiaty_lubelskie
                    4 -> R.array.powiaty_lubuskie
                    5 -> R.array.powiaty_lodzkie
                    6 -> R.array.powiaty_malopolskie
                    7 -> R.array.powiaty_mazowieckie
                    8 -> R.array.powiaty_opolskie
                    9 -> R.array.powiaty_podkarpackie
                    10 -> R.array.powiaty_podlaskie
                    11 -> R.array.powiaty_pomorskie
                    12 -> R.array.powiaty_slaskie
                    13 -> R.array.powiaty_swietokrzyskie
                    14 -> R.array.powiaty_warminsko_mazurskie
                    15 -> R.array.powiaty_wielkopolskie
                    else -> R.array.powiaty_zachodniopomorskie
                }

                val powiaty = resources.getStringArray(powiatyId)

                spinnerPow.adapter = ArrayAdapter(
                    this@FirstLaunchActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    powiaty
                )

                spinnerPow.setSelection(0)
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

            val wojPos = spinnerWoj.selectedItemPosition
            val powPos = spinnerPow.selectedItemPosition

            if (wojPos == 0) {
                Toast.makeText(this, "Wybierz województwo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (powPos == 0) {
                Toast.makeText(this, "Wybierz powiat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

                    prefs.edit()
                        .putBoolean("dane_zapisane", true)
                        .apply()

                    startActivity(Intent(this, WarningActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Błąd zapisu danych", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
