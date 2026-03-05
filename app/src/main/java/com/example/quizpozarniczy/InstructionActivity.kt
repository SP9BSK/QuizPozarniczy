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
Panel Sędziego słuzy do obsługi quizu.
Quiz moze być przeprowadzony wśród młodzieży na 2 sposoby:
▶ Na urzadzeniu opiekuna i w jego aplikacji (tu ograniczona jest liczba zawodników do 10)
▶ Na urządzeniach młodzieży i w ich aplikacjach poprzez udostępnienie przez opiekuna kodu QR z quizem ( tu jest nie ograniczona liczba zawodników)

Quiz na urządzeniu opiekuna - ustaw:
• Liczbę zawodników (1–10)
• Liczbę pytań (1–30)
• Liczbę pytań lokalnych (1–3)
• Czas quizu dla każdego zawodnika (1–30 min.)
naciśnij:
▶ START QUIZU – rozpoczyna quiz
W tym momencie swój quiz rozpoczyna Zawodnik 1, 
po wyczerpaniu pytań lub upływie czasu jest ekran z jego wynikami oraz dwoma przyciskami
przycisk POKAŻ DOBRE ODPOWIEDZI jest aktywny jeżeli zawodnik popełnił jakieś błędy i po jego naciśnięciu wyświetlane są pytania które zawodnik miał źle oraz znakiwm X jest zaznaczona odpowiedź której udzielił a znakiem V odpowiedź poprawna
Przycisk NASTĘPNY ZAWODNIK za pomocą którego grę rozpoczyna kolejny zawodnik, po ostatnim zawodniku zamiast tego przycisku jest przycisk POKAŻ WYNIKI KOŃCOWE po naciśnięciu którego otwiera się tabela z wynikami wszystkich zawodników biorących udział w quizie

Przed rozpoczęciem quizu można edytować nazwy zawodników.
▶ EDYTUJ ZAWODNIKÓW – tutaj można zmienić nazwy zawodników np. na imiona  

Quiz na urządzeniach młodzieży - ustaw:
• Liczbę pytań (1–30)
• Liczbę pytań lokalnych (1–3)
• Czas quizu dla zawodnika (1–30 min.)
• Nie ma potrzeby ustawiania liczby zawodników, jeśli przez przypadek wpiszemy jakąś liczbę to i tak quiz wygeneruje poprawny kod QR
naciśnij:
▶ UDOSTĘPNIJ QUIZ – na ekranie pojawi się kod QR który młodzież skanuje za pomocą funkcji POBIERZ QUIZ w swojej aplikacji
Aby ustalić wyniki końcowe quizu opiekun pobiera na swoje urządzenie wyniki od zawodników za poocą funkcji:
▶ SKANUJ WYNIKI Funkcja ta skanuje kody QR które pojawiają się u młodzieży po zakończeniu quizu.
Na ekranie ze skanerem znajduje się przycisk 
▶ POKAŻ WYNIKI po naciśnięciu którego pojawia się tabela z wynikami.
Wyniki można zapisać na urządzeniu opiekuna naciskając
▶ ZAPISZ WYNIKI

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
