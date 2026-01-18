package com.example.quizpozarniczy

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question("Jaki jest numer alarmowy straży pożarnej?"),
            Question("Co oznacza skrót PSP?"),
            Question("Jakim kolorem oznacza się hydrant?"),
            Question("Co robić jako pierwsze przy pożarze?"),
            Question("Kto dowodzi akcją ratowniczą?")
        )
    }
}
