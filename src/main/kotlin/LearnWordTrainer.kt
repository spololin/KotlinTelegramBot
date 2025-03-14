package org.example

import java.io.File
import kotlin.math.roundToInt

const val LEARNED_COUNT = 3
const val COUNT_ANSWERS = 4

class LearnWordTrainer {
    private var question: Question? = null
    private var words = mutableListOf<Word>()

    init {
        val wordsFile = File("words.txt")
        val rawData = wordsFile.readLines()

        rawData.forEach { word ->
            val (original, translateWord, countCorrectAnswers) = word.split("|")

            words.add(Word(original.trim(), translateWord.trim(), countCorrectAnswers.toIntOrNull() ?: 0))
        }
    }

    fun calculateStatistics(): Statistics {
        val totalCount = words.size
        val correctAnswersCount = words.filter { it.correctAnswersCount == LEARNED_COUNT }.size
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
        val notLearnedList = words.filter { it.correctAnswersCount < LEARNED_COUNT }
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.take(COUNT_ANSWERS).shuffled()
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