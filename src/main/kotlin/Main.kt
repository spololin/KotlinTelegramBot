package org.example

fun main() {
    val dictionary = Dictionary()
    val dictionaryWords = dictionary.getListWords()

    dictionaryWords.forEach { word ->
        println("Original: ${word.original}, translate: ${word.word}, count correct answers: ${word.correctAnswersCount}")
    }
}