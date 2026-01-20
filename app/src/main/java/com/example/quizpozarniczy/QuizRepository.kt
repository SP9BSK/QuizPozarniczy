package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "Numer alarmowy do straży pożarnej to:",
                listOf("998", "997", "999", "112"),
                0
            ),
            Question(
                "Kto dowodzi akcją ratowniczą?",
                listOf(
                    "Najstarszy strażak",
                    "Dowódca zastępu",
                    "Dyżurny PSP",
                    "Kierowca"
                ),
                1
            ),
            Question(
                "Kolor hełmu dowódcy OSP to:",
                listOf("Czerwony", "Biały", "Czarny", "Żółty"),
                1
            ),
            Question(
                "Co oznacza skrót PSP?",
                listOf(
                    "Państwowa Straż Pożarna",
                    "Polska Straż Pożarna",
                    "Publiczna Straż Pożarna",
                    "Pożarna Służba Państwowa"
                ),
                0
            ),
            Question(
                "Jakim środkiem gasi się pożary grupy F?",
                listOf("Woda", "Piana", "Proszek", "Tłuszcz"),
                1
            ),
            Question(
                "Kto może ogłosić alarm OSP?",
                listOf(
                    "Każdy strażak",
                    "Sołtys",
                    "Dyżurny SKKP",
                    "Dowódca JRG"
                ),
                2
            ),
            Question(
                "Co oznacza skrót OSP?",
                listOf(
                    "Ochotnicza Straż Pożarna",
                    "Obronna Straż Publiczna",
                    "Organizacja Straży Pożarnej",
                    "Ochronna Służba Pożaru"
                ),
                0
            ),
            Question(
                "Podstawowy środek gaśniczy w PSP to:",
                listOf("Woda", "Piana", "Proszek", "CO2"),
                0
            )
        )
    }
}
