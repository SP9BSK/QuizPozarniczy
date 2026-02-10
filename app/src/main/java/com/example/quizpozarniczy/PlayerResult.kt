package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.WrongAnswer

data class PlayerResult(
    val playerNumber: Int,
    val playerName: String,
    val score: Int,
    val total: Int,
    val timeSeconds: Int,
    val wrongAnswers: List<WrongAnswer>
) {
    /** Czy zawodnik odpowiedział na wszystko poprawnie */
    val isPerfect: Boolean
        get() = wrongAnswers.isEmpty()

    /** Wynik w % – pod UI */
    val percentScore: Int
        get() = if (total == 0) 0 else (score * 100) / total

    /** Czy ma sens pokazywać ekran "pokaż dobre odpowiedzi" */
    val hasWrongAnswers: Boolean
        get() = wrongAnswers.isNotEmpty()
}
