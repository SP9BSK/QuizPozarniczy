package com.example.quizpozarniczy

import android.content.Context
import org.json.JSONArray

object QuizRepository {

    fun loadQuestions(context: Context): List<Question> {
        val json = context.assets.open("questions.json")
            .bufferedReader()
            .use { it.readText() }

        val array = JSONArray(json)
        val list = mutableListOf<Question>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            val answersJson = obj.getJSONArray("answers")
            val answers = mutableListOf<String>()
            for (j in 0 until answersJson.length()) {
                answers.add(answersJson.getString(j))
            }

            list.add(
                Question(
                    question = obj.getString("question"),
                    answers = answers,
                    correctIndex = obj.getInt("correctIndex")
                )
            )
        }
        return list
    }
}
