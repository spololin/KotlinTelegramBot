package org.example

import java.io.File

class Dictionary {
    private var words = mutableListOf<Word>()

    init {
        val wordsFile = File("words.txt")
        val rawData = wordsFile.readLines()

        rawData.forEach { word ->
            val (original, translateWord, countCorrectAnswers) = word.split("|")

            words.add(Word(original, translateWord, countCorrectAnswers.toIntOrNull() ?: 0))
        }
    }

    fun getListWords(): List<Word> {
        return words
    }
}