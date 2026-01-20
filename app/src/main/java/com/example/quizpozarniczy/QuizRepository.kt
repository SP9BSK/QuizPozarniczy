package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "Numer alarmowy do straży pożarnej to:",
                listOf("998", "999", "112", "997"),
                0
            ),
            Question(
                "Czym gasi się pożar instalacji elektrycznej?",
                listOf("Wodą", "Pianą", "Gaśnicą proszkową", "Kocem"),
                2
            ),
            Question(
                "Co oznacza skrót OSP?",
                listOf(
                    "Ochotnicza Straż Pożarna",
                    "Ogólnopolska Służba Pożaru",
                    "Oficjalna Straż Państwowa",
                    "Oddział Straży Pożarnej"
                ),
                0
            ),
            Question(
                "Który gaz jest najczęściej przyczyną wybuchów w domach?",
                listOf("Tlen", "Propan-butan", "Azot", "Dwutlenek węgla"),
                1
            ),
            Question(
                "Pierwszą czynnością przy pożarze jest:",
                listOf(
                    "Gaszenie",
                    "Alarmowanie",
                    "Ewakuacja",
                    "Wyniesienie mienia"
                ),
                1
            )
        )
    }
}
