package com.example.quizpozarniczy

import com.example.quizpozarniczy.data.questionsPart1
import com.example.quizpozarniczy.data.questionsPart2
import com.example.quizpozarniczy.data.questionsPart3
import com.example.quizpozarniczy.data.questionsPart4
import com.example.quizpozarniczy.data.questionsPart5
import com.example.quizpozarniczy.data.questionsPart6
import com.example.quizpozarniczy.data.questionsPart7
import com.example.quizpozarniczy.data.questionsPart8
import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return questionsPart1 + questionsPart2 + questionsPart3 + questionsPart4 + questionsPart5 + questionsPart6 + questionsPart7 + questionsPart8
    }
}
