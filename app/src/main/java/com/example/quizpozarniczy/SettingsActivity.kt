package com.example.quizpozarniczy

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.bluetooth.BtClient
import com.example.quizpozarniczy.bluetooth.BtServer
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    // Skaner QR (dla młodzieży)
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Skanowanie anulowane", Toast.LENGTH_SHORT).show()
        } else {
            // Po zeskanowaniu QR uruchamiamy klienta Bluetooth
            connectToOpiekun(result.contents)
        }
    }

    private var btServer: BtServer? = null
    private var btClient: BtClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnExportImport = findViewById<Button>(R.id.btnExportImport)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

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

        // 2️⃣ A – QR (Bluetooth)
        btnA.text = if (isOpiekun) "UDOSTĘPNIJ PYTANIA LOKALNE (QR)" else "POBIERZ PYTANIA LOKALNE (QR)"
        btnA.setOnClickListener {
            if (isOpiekun) showQrForBluetooth() else scanLocalQuestionsQR()
        }

        // 3️⃣ EXPORT / IMPORT JSON
        if (isOpiekun) {
            btnExportImport.text = "UDOSTĘPNIJ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { exportLocalQuestions() }
        } else {
            btnExportImport.text = "POBIERZ PYTANIA LOKALNE"
            btnExportImport.setOnClickListener { importLocalQuestions() }
        }

        // 4️⃣ REGULAMIN
        btnRegulamin.setOnClickListener {
            startActivity(Intent(this, RegulaminActivity::class.java))
        }
    }

    // =========================
    // EXPORT – JSON
    // =========================
    private fun exportLocalQuestions() {
        val generalQuestions = QuizRepository.getQuestions(localCount = 0)
        val localQuestions = LocalQuestionsRepository.questions

        if (localQuestions.isEmpty()) {
            Toast.makeText(this, "Brak pytań lokalnych do eksportu", Toast.LENGTH_LONG).show()
            return
        }

        val uri = QuizExporter.createExportJson(this, generalQuestions, localQuestions) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Udostępnij pytania lokalne"))
    }

    // =========================
    // IMPORT – JSON
    // =========================
    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val (_, localQuestions) = QuizImporter.importQuiz(this, inputStream)
                if (localQuestions.isNotEmpty()) {
                    LocalQuestionsRepository.questions.clear()
                    LocalQuestionsRepository.questions.addAll(localQuestions)
                    LocalQuestionsRepository.save(this)
                }
                Toast.makeText(
                    this,
                    "Zaimportowano ${localQuestions.size} pytań lokalnych",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // OPIEKUN – QR do Bluetooth
    // =========================
    private fun showQrForBluetooth() {
        val sessionId = BtServer.startServer(this, LocalQuestionsRepository.questions) // uruchamia BtServer
        val dialog = Dialog(this)
        val imageView = ImageView(this)
        val bitmap = BtServer.generateQrForSession(sessionId)
        imageView.setImageBitmap(bitmap)
        dialog.setContentView(imageView)
        dialog.setTitle("Skanuj QR na urządzeniu młodzieży")
        dialog.show()
    }

    // =========================
    // MŁODZIEŻ – Skaner QR
    // =========================
    private fun scanLocalQuestionsQR() {
        val options = ScanOptions().apply {
            setPrompt("Zeskanuj kod QR z opiekuna")
            setBeepEnabled(true)
            setOrientationLocked(true)
        }
        barcodeLauncher.launch(options)
    }

    // =========================
    // MŁODZIEŻ – połączenie Bluetooth
    // =========================
    private fun connectToOpiekun(sessionId: String) {
        btClient = BtClient(this, sessionId) { jsonQuestions ->
            // Po otrzymaniu pytań zapisujemy lokalnie
            val (_, localQuestions) = QuizImporter.importQuizFromString(jsonQuestions)
            if (localQuestions.isNotEmpty()) {
                LocalQuestionsRepository.questions.clear()
                LocalQuestionsRepository.questions.addAll(localQuestions)
                LocalQuestionsRepository.save(this)
            }
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Pobrano ${localQuestions.size} pytań lokalnych od opiekuna",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        btClient?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        btServer?.stopServer()
        btClient?.stop()
    }
}
