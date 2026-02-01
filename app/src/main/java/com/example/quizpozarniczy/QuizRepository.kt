package com.example.quizpozarniczy

import com.example.quizpozarniczy.data.DefaultLocalQuestions
import com.example.quizpozarniczy.data.questionsPart1
import com.example.quizpozarniczy.data.questionsPart2
import com.example.quizpozarniczy.data.questionsPart3
import com.example.quizpozarniczy.data.questionsPart4
import com.example.quizpozarniczy.data.questionsPart5
import com.example.quizpozarniczy.data.questionsPart6
import com.example.quizpozarniczy.data.questionsPart7
import com.example.quizpozarniczy.data.questionsPart8
import com.example.quizpozarniczy.data.questionsPart9
import com.example.quizpozarniczy.data.questionsPart10
import com.example.quizpozarniczy.data.questionsPart11
import com.example.quizpozarniczy.data.questionsPart12
import com.example.quizpozarniczy.data.questionsPart13
import com.example.quizpozarniczy.data.questionsPart14
import com.example.quizpozarniczy.data.questionsPart15
import com.example.quizpozarniczy.model.Question

object QuizRepository {

    /**
     * @param localCount ile pytań lokalnych dodać (1–3)
     */
    fun getQuestions(localCount: Int = 0): List<Question> {

        val baseQuestions =
            questionsPart1 +
            questionsPart2 +
            questionsPart3 +
            questionsPart4 +
            questionsPart5 +
            questionsPart6 +
            questionsPart7 +
            questionsPart8 +
            questionsPart9 +
            questionsPart10 +
            questionsPart11 +
            questionsPart12 +
            questionsPart13 +
            questionsPart14 +
            questionsPart15

        val localQuestions = DefaultLocalQuestions.questions
            .shuffled()
            .take(localCount)
            .map { local ->
                Question(
                    text = local.fullQuestionNoQuotes(), // 🔥 BEZ CUDZYSŁOWÓW
                    answers = local.answers,
                    correctIndex = local.correctIndex
                )
            }

        return (baseQuestions + localQuestions).shuffled()
    }
}
