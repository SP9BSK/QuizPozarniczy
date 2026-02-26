package com.example.quizpozarniczy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val txtRanking = findViewById<TextView>(R.id.txtRanking)
        val btnBack = findViewById<Button>(R.id.btnBackToJudge)
        btnBack.text = "POWRÓT NA STRONĘ GŁÓWNĄ"
        val imgQR = findViewById<ImageView>(R.id.imgQR)

        txtRanking.typeface = Typeface.MONOSPACE

        if (QuizSession.results.isEmpty()) {
            txtRanking.text = "Brak wyniku do wyświetlenia."
            btnBack.setOnClickListener {
                QuizSession.resetTournament()
                val intent = Intent(this, StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            return
        }

        val result = QuizSession.results.last()

        val wynikTekst = """
            Zawodnik: ${result.playerName}
            Punkty:   ${result.score}/${result.total}
            Czas:     ${formatTime(result.timeSeconds)}
        """.trimIndent()

        txtRanking.text = wynikTekst

        // JSON do QR
        val json = JSONObject().apply {
            put("player", result.playerName)
            put("score", result.score)
            put("total", result.total)
            put("time", result.timeSeconds)
        }.toString()

        // Generowanie QR
        val qrBitmap = generateQR(json)
        imgQR.setImageBitmap(qrBitmap)

        btnBack.setOnClickListener {
            QuizSession.resetTournament()
            val intent = Intent(this, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun generateQR(text: String): Bitmap {
        val size = 600
        val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bits[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        return bmp
    }
}
