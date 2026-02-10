package com.example.quizpozarniczy.util

import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader

data class SinglePlayerQuiz(
    val playerName: String,
    val timeSeconds: Int,
    val questions: List<Question>
)

object QuizImporter {

    private val gson = Gson()

    fun importSinglePlayerQuiz(inputStream: InputStream): SinglePlayerQuiz? {
        return try {

            val reader = InputStreamReader(inputStream)
            val root = gson.fromJson(reader, Map::class.java)

            val metadata = root["metadata"] as Map<*, *>
            val playerName = metadata["playerName"] as String
            val timeSeconds = (metadata["timeSeconds"] as Double).toInt()

            val questionsJson = root["questions"]
            val questions = gson.fromJson(
                gson.toJson(questionsJson),
                Array<Question>::class.java
            ).toList()

            SinglePlayerQuiz(
                playerName = playerName,
                timeSeconds = timeSeconds,
                questions = questions
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
