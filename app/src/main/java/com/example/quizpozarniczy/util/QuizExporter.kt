package com.example.quizpozarniczy.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

object QuizExporter {

    private val gson = Gson()

    fun createSinglePlayerQuizJson(
        context: Context,
        playerName: String,
        questions: List<Question>,
        timeSeconds: Int
    ): Uri? {
        return try {

            val data = mapOf(
                "metadata" to mapOf(
                    "playerName" to playerName,
                    "timeSeconds" to timeSeconds
                ),
                "questions" to questions
            )

            val file = File(context.cacheDir, "QuizSinglePlayer.json")

            FileWriter(file).use { writer ->
                gson.toJson(data, writer)
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
