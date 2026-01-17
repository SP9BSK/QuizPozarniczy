package com.example.quizpozarniczy

data class Question(
    val question: String,
    val answers: List<String>,
    val correct: Int
)

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "Jaki numer alarmowy ma straż pożarna?",
                listOf("998", "999", "112", "997"),
                0
            ),
            Question(
                "Co oznacza skrót OSP?",
                listOf(
                    "Ochotnicza Straż Pożarna",
                    "Oficjalna Straż Państwowa",
                    "Organizacja Strażacka",
                    "Oddział Straży Publicznej"
                ),
                0
            ),
            Question(
                "Jaki gaz jest niezbędny do spalania?",
                listOf("Azot", "Tlen", "Dwutlenek węgla", "Hel"),
                1
            ),
            Question(
                "Jakim kolorem oznacza się hydrant?",
                listOf("Niebieskim", "Czerwonym", "Zielonym", "Żółtym"),
                1
            )
        )
    }
}
