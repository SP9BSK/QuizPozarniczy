package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "Co jest pierwszym środkiem gaśniczym?",
                listOf("Woda", "Piana", "Piasek", "Proszek"),
                0
            ),
            Question(
                "Numer alarmowy straży pożarnej to:",
                listOf("997", "998", "999", "112"),
                1
            ),
            Question(
                "Kolor hełmu dowódcy to:",
                listOf("Czarny", "Biały", "Czerwony", "Żółty"),
                1
            )
        )
    }
}
