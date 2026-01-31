package com.example.quizpozarniczy.data

import android.content.Context
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LocalQuestionsRepository {

    private const val PREFS_NAME = "local_questions_prefs"
    private const val KEY_QUESTIONS = "questions_json"

    val questions: MutableList<LocalQuestion> = mutableListOf()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_QUESTIONS, null)

        questions.clear()

        if (json != null) {
            val type = object : TypeToken<MutableList<LocalQuestion>>() {}.type
            questions.addAll(Gson().fromJson(json, type))
        } else {
            questions.addAll(DefaultLocalQuestions.questions)
        }
    }

    fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_QUESTIONS, Gson().toJson(questions))
            .apply()
    }

    /**
     * 🔥 TYLKO TĄ METODĄ lokalne pytania trafiają do quizu
     * ❗ BEZ CUDZYSŁOWÓW
     */
    fun toQuizQuestions(limit: Int): List<Question> {
        return questions
            .shuffled()
            .take(limit)
            .map { local ->
                Question(
                    text = local.fullQuestionNoQuotes(),
                    answers = local.answers.toList(),
                    correctIndex = local.correctIndex
                )
            }
    }
}
