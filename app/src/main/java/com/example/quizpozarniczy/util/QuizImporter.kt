data class SinglePlayerQuiz(
    val playerName: String,
    val timeSeconds: Int,
    val questions: List<Question>
)

fun importSinglePlayerQuiz(inputStream: InputStream): SinglePlayerQuiz? {
    return try {
        val reader = InputStreamReader(inputStream)
        val jsonObj = gson.fromJson(reader, Map::class.java)

        val metadata = jsonObj["metadata"] as Map<*, *>
        val playerName = metadata["playerName"] as String
        val timeSeconds = (metadata["timeSeconds"] as Double).toInt()

        val questionList = jsonObj["questions"] as List<Map<String, Any>>
        val questions = mutableListOf<Question>()
        questionList.forEach { q ->
            if (q["type"] == "general") {
                questions.add(
                    Question(
                        text = q["text"] as String,
                        answers = (q["answers"] as List<String>),
                        correctIndex = (q["correctIndex"] as Double).toInt()
                    )
                )
            }
        }

        SinglePlayerQuiz(playerName, timeSeconds, questions)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
