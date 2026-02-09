fun createSinglePlayerQuizJson(
    context: Context,
    playerName: String,
    generalQuestions: List<Question>,
    localQuestions: List<LocalQuestion>,
    timeSeconds: Int
): Uri? {
    return try {
        val exportList = mutableListOf<ExportQuestion>()

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

        val metadata = mapOf(
            "playerName" to playerName,
            "timeSeconds" to timeSeconds
        )

        val file = File(context.cacheDir, "QuizSinglePlayer.json")
        FileWriter(file).use { writer ->
            gson.toJson(mapOf(
                "metadata" to metadata,
                "questions" to exportList
            ), writer)
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
