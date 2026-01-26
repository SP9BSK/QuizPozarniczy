package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.Question

val questionsPart2 = listOf(

    Question(
        text = "Jaka jest praktyczna wysokość ssania motopomp strażackich?",
        answers = listOf("7,5 m", "7 m", "6,5 m"),
        correctIndex = 0
    ),

    Question(
        text = "Drabina dwuprzęsłowa wysuwana może być sprawiona jako:",
        answers = listOf("Przyparta", "Odchylona", "Wolnostojąca"),
        correctIndex = 2
    ),

    Question(
        text = "Jaka jest normatywna szerokość poziomych dróg ewakuacyjnych w nowo projektowanych budynkach?",
        answers = listOf("1,5 m", "1,4 m", "1,8 m"),
        correctIndex = 1
    ),

    Question(
        text = "Związek Ochotniczych Straży Pożarnych został powołany w roku:",
        answers = listOf("1948", "1958", "1956"),
        correctIndex = 2
    ),

    Question(
        text = "Co stanie się z płomieniem świecy, jeżeli w pomieszczeniu zwiększymy stężenie tlenu?",
        answers = listOf(
            "Płomień wydłuży się",
            "Płomień skróci się i rozszerzy",
            "Płomień zgaśnie"
        ),
        correctIndex = 0
    )

    // kolejne pytania…
)
