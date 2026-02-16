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
import com.example.quizpozarniczy.data.questionsPart16
import com.example.quizpozarniczy.data.questionsPart17
import com.example.quizpozarniczy.data.questionsPart18
import com.example.quizpozarniczy.data.questionsPart19
import com.example.quizpozarniczy.data.questionsPart20
import com.example.quizpozarniczy.data.questionsPart21
import com.example.quizpozarniczy.model.Question

object QuizRepository {

    // ===============================
    // 🔥 WERSJA DO QUIZU TURNIEJOWEGO
    // ===============================
    fun getQuestions(totalLimit: Int, localCount: Int): List<Question> {

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
            questionsPart15 +
            questionsPart16 +
            questionsPart17 +
            questionsPart18 +
            questionsPart19 +
            questionsPart20 +
            questionsPart21

        // 🔒 Zabezpieczenie przed głupimi wartościami
        val safeLocalCount = localCount
            .coerceAtLeast(0)
            .coerceAtMost(DefaultLocalQuestions.questions.size)

        val safeGeneralLimit = (totalLimit - safeLocalCount)
            .coerceAtLeast(0)

        val selectedLocal = DefaultLocalQuestions.questions
            .shuffled()
            .take(safeLocalCount)
            .map { local ->
                Question(
                    text = local.fullQuestionNoQuotes(),
                    answers = local.answers,
                    correctIndex = local.correctIndex
                )
            }

        val selectedGeneral = baseQuestions
            .shuffled()
            .take(safeGeneralLimit)

        return (selectedLocal + selectedGeneral).shuffled()
    }

    // ===============================
    // 📘 WERSJA DO TRYBU NAUKI
    // ===============================
    fun getQuestions(): List<Question> {

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
            questionsPart15 +
            questionsPart16 +
            questionsPart17 +
            questionsPart18 +
            questionsPart19 +
            questionsPart20 +
            questionsPart21

        val localQuestions = DefaultLocalQuestions.questions.map { local ->
            Question(
                text = local.fullQuestionNoQuotes(),
                answers = local.answers,
                correctIndex = local.correctIndex
            )
        }

        return (baseQuestions + localQuestions).shuffled()
    }
}
