class JudgeActivity : AppCompatActivity() {

    fun startQuiz(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("QUESTIONS", 10)
        intent.putExtra("TIME", 300)
        startActivity(intent)
    }
}
