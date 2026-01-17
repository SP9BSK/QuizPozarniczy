package com.example.quizpozarniczy.util

import android.content.Context
import com.example.quizpozarniczy.model.Question
import org.json.JSONArray

object QuizRepository {

    fun load(context: Context): List<Question> {
        val json = context.assets.open("pytania.json")
            .bufferedReader().use { it.readText() }

        val array = JSONArray(json)
        val list = mutableListOf<Question>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            list.add(
                Question(
                    o.getString("question"),
                    listOf(
                        o.getString("a"),
                        o.getString("b"),
                        o.getString("c"),
                        o.getString("d")
                    ),
                    o.getInt("correct")
                )
            )
        }
        return list.shuffled()
    }
}
