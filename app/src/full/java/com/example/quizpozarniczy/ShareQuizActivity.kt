package com.example.quizpozarniczy

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class ShareQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_quiz)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        val qrData = intent.getStringExtra("QR_DATA") ?: return
        val imageView = findViewById<ImageView>(R.id.imgQr)

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 800, 800)

        val bmp = Bitmap.createBitmap(800, 800, Bitmap.Config.RGB_565)
        for (x in 0 until 800) {
            for (y in 0 until 800) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }

        imageView.setImageBitmap(bmp)
    }
}
