package com.example.quizpozarniczy

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                text = "Jaki numer alarmowy ma Straż Pożarna?",
                answers = listOf("998", "997", "999", "112"),
                correctIndex = 0
            ),
            Question(
                text = "Co oznacza skrót OSP?",
                answers = listOf(
                    "Ochotnicza Straż Pożarna",
                    "Ogólna Służba Pożarna",
                    "Oddział Specjalny PSP",
                    "Organizacja Strażacka Państwowa"
                ),
                correctIndex = 0
            ),
            Question(
                text = "Kto dowodzi akcją ratowniczą?",
                answers = listOf(
                    "Najstarszy stażem",
                    "Dowódca wyznaczony",
                    "Każdy strażak",
                    "Dyżurny PSP"
                ),
                correctIndex = 1
            )
        )
    }
}
