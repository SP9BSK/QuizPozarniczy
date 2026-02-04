package com.example.quizpozarniczy.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter

object QuizExporter {

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls() // żeby null-e w quotedValue1/2 były zapisane
        .create()

    data class ExportQuestion(
        val type: String,
        val id: Int? = null,
        val text: String? = null,
        val prefix: String? = null,
        val quotedValue1: String? = null,
        val middle: String? = null,
        val quotedValue2: String? = null,
        val suffix: String? = null,
        val answers: List<String>,
        val correctIndex: Int
    )

    data class ExportPackage(
        val questions: List<ExportQuestion>
    )

    /**
     * Tworzy JSON z list pytań ogólnych i lokalnych
     */
    fun createExportJson(
        context: Context,
        generalQuestions: List<Question>,
        localQuestions: List<LocalQuestion>
    ): Uri? {
        try {
            val exportList = mutableListOf<ExportQuestion>()

            // Dodaj pytania ogólne
            generalQuestions.forEach { q ->
                exportList.add(
                    ExportQuestion(
                        type = "general",
                        text = q.text,
                        answers = q.answers,
                        correctIndex = q.correctIndex
                    )
                )
            }

            // Dodaj pytania lokalne
            localQuestions.forEach { lq ->
                exportList.add(
                    ExportQuestion(
                        type = "local",
                        id = lq.id,
                        prefix = lq.prefix,
                        quotedValue1 = lq.quotedValue1,
                        middle = lq.middle,
                        quotedValue2 = lq.quotedValue2,
                        suffix = lq.suffix,
                        answers = lq.answers,
                        correctIndex = lq.correctIndex
                    )
                )
            }

            val exportPackage = ExportPackage(questions = exportList)

            // Zapis do pliku w cache (można udostępnić przez FileProvider)
            val file = File(context.cacheDir, "QuizMDP.json")
            val writer = FileWriter(file)
            gson.toJson(exportPackage, writer)
            writer.flush()
            writer.close()

            // Zwróć URI do udostępnienia
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
