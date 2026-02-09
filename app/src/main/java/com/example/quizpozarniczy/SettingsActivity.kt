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
import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import com.example.quizpozarniczy.util.QuizExporter
import com.example.quizpozarniczy.util.QuizImporter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class SettingsActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Skanowanie anulowane", Toast.LENGTH_SHORT).show()
        } else {
            connectToOpiekun(result.contents)
        }
    }

    private var btClient: BtClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val btnEditOrB = findViewById<Button>(R.id.btnEditOrB)
        val btnA = findViewById<Button>(R.id.btnA)
        val btnExportImport = findViewById<Button>(R.id.btnExportImport)
        val btnRegulamin = findViewById<Button>(R.id.btnRegulamin)

        // 🔹 Inicjalizacja
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

        // 2️⃣ A – QR Bluetooth
        btnA.text =
            if (isOpiekun) "UDOSTĘPNIJ PYTANIA LOKALNE (QR)"
            else "POBIERZ PYTANIA LOKALNE (QR)"

        btnA.setOnClickListener {
            if (isOpiekun) showQrForBluetooth()
            else scanLocalQuestionsQR()
        }

        // 3️⃣ EXPORT / IMPORT
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
        val localQuestions = DefaultLocalQuestions.questions

        val uri = QuizExporter.createExportJson(this, generalQuestions, localQuestions) ?: return

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Udostępnij pytania lokalne"))
    }

    private fun importLocalQuestions() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }

    // =========================
    // OPIEKUN – QR
    // =========================
    private fun showQrForBluetooth() {
        val qrData = BtServer.startServer(this, DefaultLocalQuestions.questions)
        val dialog = Dialog(this)
        val imageView = ImageView(this)
        imageView.setImageBitmap(BtServer.generateQrForSession(qrData))
        dialog.setContentView(imageView)
        dialog.show()
    }

    // =========================
    // MŁODZIEŻ – QR SCAN
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
    // MŁODZIEŻ – BT CONNECT
    // =========================
    private fun connectToOpiekun(qrData: String) {
        btClient = BtClient(this, qrData) { json ->
            val (_, localQuestions) = QuizImporter.importQuizFromString(json)
            LocalQuestionsRepository.questions.clear()
            LocalQuestionsRepository.questions.addAll(localQuestions)
            LocalQuestionsRepository.save(this)

            runOnUiThread {
                Toast.makeText(
                    this,
                    "Pobrano ${localQuestions.size} pytań lokalnych",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        btClient?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        btClient?.stop()
    }
}
