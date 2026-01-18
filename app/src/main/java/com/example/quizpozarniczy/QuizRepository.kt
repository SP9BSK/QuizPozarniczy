package com.example.quizpozarniczy

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "Jaki jest numer alarmowy straży pożarnej?",
                listOf("997", "998", "112", "999"),
                1
            ),
            Question(
                "Co oznacza skrót PSP?",
                listOf(
                    "Państwowa Straż Pożarna",
                    "Publiczna Służba Pożarna",
                    "Państwowa Służba Porządkowa",
                    "Polska Straż Pożarna"
                ),
                0
            ),
            Question(
                "Jakim kolorem oznacza się hydrant?",
                listOf("Niebieski", "Zielony", "Czerwony", "Żółty"),
                2
            )
        )
    }
}
