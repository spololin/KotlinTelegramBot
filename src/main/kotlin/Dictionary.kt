package org.example

import java.io.File
import kotlin.math.roundToInt

const val LEARNED_COUNT = 3

class Dictionary {
    private var words = mutableListOf<Word>()

    init {
        val wordsFile = File("words.txt")
        val rawData = wordsFile.readLines()

        rawData.forEach { word ->
            val (original, translateWord, countCorrectAnswers) = word.split("|")

            words.add(Word(original.trim(), translateWord.trim(), countCorrectAnswers.toIntOrNull() ?: 0))
        }
    }

    fun calculateStatistics() {
        val totalCount = words.size
        val correctAnswersCount = words.filter { it.correctAnswersCount == LEARNED_COUNT }.size
        val percent = (correctAnswersCount.toDouble() / totalCount.toDouble() * 100.0).roundToInt()

        println(String.format("Выучено %d из %d | %d", correctAnswersCount, words.size, percent))
    }

    fun processLearning() {
        val notLearnedList = words.filter { it.correctAnswersCount < LEARNED_COUNT }

        if (notLearnedList.isEmpty()) {
            println("Все слова в словаре выучены")
            return
        }

        for (word in notLearnedList) {
            val questionWords = notLearnedList.filter { it != word }.shuffled().take(LEARNED_COUNT)
            val questionAnswers = (questionWords.map { it.word } + listOf(word.word)).shuffled()
            val correctAnswerIdx = questionAnswers.indexOfFirst { it == word.word }

            println()
            println("${word.original}:")

            questionAnswers.forEachIndexed { idx, it -> println(" ${idx + 1} - $it") }
            println("------------")
            println(" 0 - Меню")
            val answer = readln().toIntOrNull()

            if (answer != null) {
                if (answer == 0) {
                    return
                }

                if (answer - 1 == correctAnswerIdx) {
                    println("Правильно!")
                    word.correctAnswersCount++
                    saveDictionary()
                } else {
                    println("Неправильно! ${word.original} - это ${word.word}")
                }
            }
        }
    }

    private fun saveDictionary() {
        val wordsFile = File("words.txt")
        wordsFile.writeText("")

        words.forEach { word ->
            wordsFile.appendText("${word.original}|${word.word}|${word.correctAnswersCount}\n")
        }
    }
}