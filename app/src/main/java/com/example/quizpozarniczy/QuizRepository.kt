package com.example.quizpozarniczy

import com.example.quizpozarniczy.data.questionsPart1
import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return questionsPart1
    }
}
