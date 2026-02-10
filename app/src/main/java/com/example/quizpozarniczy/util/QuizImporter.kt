package com.example.quizpozarniczy.util

import android.content.Context
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader

object QuizImporter {

    private val gson = Gson()

    fun importQuiz(
        context: Context,
        inputStream: InputStream
    ): Pair<List<Question>, List<LocalQuestion>>? {
        return try {

            val reader = InputStreamReader(inputStream)
            val root: Map<String, Any> =
                gson.fromJson(reader, object : TypeToken<Map<String, Any>>() {}.type)

            val generalJson = gson.toJson(root["generalQuestions"])
            val localJson = gson.toJson(root["localQuestions"])

            val generalQuestions: List<Question> =
                gson.fromJson(generalJson, object : TypeToken<List<Question>>() {}.type)

            val localQuestions: List<LocalQuestion> =
                gson.fromJson(localJson, object : TypeToken<List<LocalQuestion>>() {}.type)

            Pair(generalQuestions, localQuestions)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
