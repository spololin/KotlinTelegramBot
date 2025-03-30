package org.example

import java.io.File
import kotlin.math.roundToInt

class LearnWordTrainer(
    private val learnedCount: Int = 3,
    private val countAnswersInQuestion: Int = 4
) {
    var question: Question? = null
    private var words = mutableListOf<Word>()

    init {
        try {
            val wordsFile = File("words.txt")
            val rawData = wordsFile.readLines()

            rawData.forEach { word ->
                val (original, translateWord, countCorrectAnswers) = word.split("|")

                words.add(Word(original.trim(), translateWord.trim(), countCorrectAnswers.toIntOrNull() ?: 0))
            }
        } catch(e: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }

    fun calculateStatistics(): Statistics {
        val totalCount = words.size
        val correctAnswersCount = words.filter { it.correctAnswersCount == learnedCount }.size
        val percent = (correctAnswersCount.toDouble() / totalCount.toDouble() * 100.0).roundToInt()

        return Statistics(totalCount, correctAnswersCount, percent)
    }

    private fun saveDictionary() {
        val wordsFile = File("words.txt")
        wordsFile.writeText("")

        words.forEach { word ->
            wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = words.filter { it.correctAnswersCount < learnedCount }
        if (notLearnedList.isEmpty()) return null

        val questionWords = if (notLearnedList.size < countAnswersInQuestion) {
            val learnedList = words.filter { it.correctAnswersCount >= learnedCount }.shuffled()
            notLearnedList.shuffled().take(countAnswersInQuestion) +
                    learnedList.take(countAnswersInQuestion - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countAnswersInQuestion)
        }.shuffled()

        val questionAnswers = questionWords.random()

        question = Question(
            variants = questionWords,
            correctAnswer = questionAnswers
        )

        return question
    }

    fun checkAnswer(answerIdx: Int?): Boolean {
        return question?.let {
            val correctAnswerIdx = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerIdx == answerIdx) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary()
                true
            } else {
                false
            }
        } ?: false
    }
}