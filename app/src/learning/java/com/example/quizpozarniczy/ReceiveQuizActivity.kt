package com.example.quizpozarniczy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizpozarniczy.data.LocalQuestionsRepository
import org.json.JSONObject
import androidx.activity.result.contract.ActivityResultContracts


class ReceiveQuizActivity : AppCompatActivity() {

    private lateinit var txtInfo: TextView
    private lateinit var btnScan: Button
    private lateinit var btnStart: Button
    private lateinit var btnName: Button


    private var timeSeconds = 60
    private var ids: List<Int> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_quiz)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        txtInfo = findViewById(R.id.txtInfo)
        btnScan = findViewById(R.id.btnScan)
        btnStart = findViewById(R.id.btnStart)

        btnStart.isEnabled = false

        btnScan.setOnClickListener {
            startQrScan()
        }

        btnStart.setOnClickListener {
            startQuiz()
        }
        btnName = findViewById(R.id.btnName)

       btnName.setOnClickListener {
         askForName()
       }

    }

    private val qrLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.getStringExtra("QR_DATA")
            if (data != null) {
                onQrScanned(data)
            }
        }
    }

private fun startQrScan() {
    qrLauncher.launch(Intent(this, QrScannerActivity::class.java))
}


    private fun onQrScanned(text: String) {
        val json = JSONObject(text)

        timeSeconds = json.getInt("time")

        val arr = json.getJSONArray("ids")
        ids = List(arr.length()) { i -> arr.getInt(i) }

        val minutes = timeSeconds / 60
        val seconds = timeSeconds % 60

         txtInfo.text = "Pobrano quiz:\n" +
                      "• Pytań: ${ids.size}\n" +
                      "• Czas: %02d:%02d".format(minutes, seconds)


        btnStart.isEnabled = true
    }

    private fun startQuiz() {
      val all = QuizRepository.getAllQuestions()
        val selected = all.filter { it.id in ids }

        QuizSession.questions.clear()
        QuizSession.questions.addAll(selected)
        QuizSession.currentPlayer = 1
        QuizSession.totalPlayers = 1
        QuizSession.results.clear()

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("TIME_SECONDS", timeSeconds)
        intent.putExtra("QUESTIONS", selected.size)
        startActivity(intent)
        finish()
    }
    private fun askForName() {
    val dialog = android.app.AlertDialog.Builder(this)
    dialog.setTitle("Twoje imię")

    val input = android.widget.EditText(this)
    input.hint = "Wpisz swoje imię"
    dialog.setView(input)

    dialog.setPositiveButton("OK") { _, _ ->
        val name = input.text.toString().ifBlank { "Zawodnik" }
        QuizSession.playerNames.clear()
        QuizSession.playerNames.add(name)

        txtInfo.text = txtInfo.text.toString() + "\n• Imię: $name"
    }

    dialog.setNegativeButton("Anuluj", null)
    dialog.show()
}

}
