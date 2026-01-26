package com.example.quizpozarniczy

import com.example.quizpozarniczy.data.QuestionsPart1
import com.example.quizpozarniczy.data.QuestionsPart2

object QuizRepository {
    fun getQuestions(): List<Question> {
        return QuestionsPart1.questions + QuestionsPart2.questions
    }
}
