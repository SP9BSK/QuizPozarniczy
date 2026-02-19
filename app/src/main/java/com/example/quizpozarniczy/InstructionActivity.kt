package com.example.quizpozarniczy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager

class InstructionActivity : AppCompatActivity() {

    private val isOpiekun: Boolean
        get() = BuildConfig.APPLICATION_ID.contains("opiekun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val tvContent = findViewById<TextView>(R.id.tvInstruction)

        tvContent.text = if (isOpiekun) {
            getOpiekunInstruction()
        } else {
            getMlodziezInstruction()
        }
    }

    private fun getOpiekunInstruction(): String {
        return """
📘 INSTRUKCJA – OPIEKUN

━━━━━━━━━━━━━━━━━━

1️⃣ PANEL SĘDZIEGO

Ustaw:
• Liczbę zawodników (1–10)
• Liczbę pytań (1–30)
• Liczbę pytań lokalnych (1–3)
• Czas quizu dla każdego zawodnika (1–30 min.)

▶ START QUIZU – rozpoczyna quiz  
✏ EDYTUJ ZAWODNIKÓW – tutaj można zmienić nazwy zawodników np. na imiona  
🚫 UDOSTĘPNIJ QUIZ – funkcja w przygotowaniu  

━━━━━━━━━━━━━━━━━━

2️⃣ TRYB NAUKI

Do wyboru są 2 tryby:
• Pytania ogólne  
• Pytania lokalne  

✅ Aplikacja zapisuje postęp nauki.  
✔ Pytania z dobrą odpowiedzią nie są ponownie losowane.  
❌ Błędne pytania będą pojawiać się ponownie.  

📊 U góry ekranu widoczny jest postęp nauki.

━━━━━━━━━━━━━━━━━━

3️⃣ USTAWIENIA

✏ EDYCJA PYTAŃ LOKALNYCH – tutaj należy edytować pytania lokalne pod kontem własnej jednostki. 
Należy to zrobić przed udostępnieniem pytań lokalnych młodzieży. 
📤 UDOSTĘPNIJ PYTANIA LOKALNE – za pomocą tej funkcji wysyłamy pytania lokalne do młodzieży.    
UWAGA!! niektóre metody udostępniania np. przez ⚠ WhatsApp nie pozwalają pobrać pliku bezpośrednio na urządzenie.
W takim wypadku na apce Młodzież nalezy to zrobić wybierając w WhatsApp funkcję UDOSTĘPNIJ i zapisać na dysku google
📜 REGULAMIN – zasady korzystania oraz polityka prywatności.
        """.trimIndent()
    }

    private fun getMlodziezInstruction(): String {
        return """
📘 INSTRUKCJA – MŁODZIEŻ

━━━━━━━━━━━━━━━━━━

1️⃣ EKRAN GŁÓWNY

• A – przycisk niewykorzystywany  
• TRYB NAUKI  
• USTAWIENIA  

━━━━━━━━━━━━━━━━━━

2️⃣ TRYB NAUKI

Do wyboru są 2 tryby:
• Pytania ogólne  
• Pytania lokalne (po imporcie od Opiekuna)

✅ Aplikacja zapisuje postęp nauki.  
✔ Pytania z dobrą odpowiedzią nie są ponownie losowane.  
❌ Błędne pytania pojawiają się ponownie.  

📊 U góry ekranu widoczny jest postęp nauki.

━━━━━━━━━━━━━━━━━━

3️⃣ USTAWIENIA

📥 POBIERZ PYTANIA LOKALNE – po zapisaniu pytań lokalnych na urządzeniu należy wskazać odpowiedni plik. 
UWAGA!! niektóre metody udostępniania np. przez ⚠ WhatsApp nie pozwalają pobrać pliku bezpośrednio na urządzenie.
Nalezy to zrobić wybierając w WhatsApp funkcję UDOSTĘPNIJ i zapisać na dysku google  
🔘 B – funkcja w przygotowaniu  
📜 REGULAMIN – zasady korzystania
        """.trimIndent()
    }
}
