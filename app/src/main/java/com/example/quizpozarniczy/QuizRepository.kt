package com.example.quizpozarniczy

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question("Czy strażak może pracować bez hełmu?", false),
            Question("Czy numer alarmowy to 112?", true),
            Question("Czy pożarów nie gasi się wodą?", false),
            Question("Czy OSP jest częścią KSRG?", true),
            Question("Czy aparat ODO służy do oddychania?", true)
        )
    }
}
