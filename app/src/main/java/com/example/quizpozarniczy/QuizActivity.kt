class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var index = 0
    private var score = 0

    override fun onBackPressed() {} // blokada cofania

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questions = QuizRepository.load(this)
        showQuestion()
    }

    private fun showQuestion() {
        val q = questions[index]
        findViewById<TextView>(R.id.tvQuestion).text = q.question

        val buttons = listOf(
            findViewById<Button>(R.id.b1),
            findViewById<Button>(R.id.b2),
            findViewById<Button>(R.id.b3),
            findViewById<Button>(R.id.b4)
        )

        buttons.forEachIndexed { i, b ->
            b.text = q.answers[i]
            b.setOnClickListener { answer(i) }
        }
    }

    private fun answer(selected: Int) {
        if (selected == questions[index].correctIndex) {
            score++
        }
        index++
        if (index < questions.size) {
            showQuestion()
        } else {
            finish()
        }
    }
}
