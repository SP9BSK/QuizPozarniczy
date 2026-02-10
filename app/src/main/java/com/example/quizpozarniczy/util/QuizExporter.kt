package com.example.quizpozarniczy.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

object QuizExporter {

    private val gson = Gson()

    fun createExportJson(
        context: Context,
        generalQuestions: List<Question>,
        localQuestions: List<LocalQuestion>
    ): Uri? {
        return try {

            val data = mapOf(
                "generalQuestions" to generalQuestions,
                "localQuestions" to localQuestions
            )

            val file = File(context.cacheDir, "LocalQuestionsExport.json")

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
