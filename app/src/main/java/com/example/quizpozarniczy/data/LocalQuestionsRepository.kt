package com.example.quizpozarniczy.data

import android.content.Context
import com.example.quizpozarniczy.model.LocalQuestion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LocalQuestionsRepository {

    private const val PREFS_NAME = "local_questions_prefs"
    private const val KEY_QUESTIONS = "questions_json"

    val questions: MutableList<LocalQuestion> = mutableListOf()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_QUESTIONS, null)

        if (json != null) {
            val type = object : TypeToken<MutableList<LocalQuestion>>() {}.type
            questions.clear()
            questions.addAll(Gson().fromJson(json, type))
        } else {
            questions.clear()
            questions.addAll(DefaultLocalQuestions.questions)
        }
    }

    fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_QUESTIONS, Gson().toJson(questions))
            .apply()
    }
}
