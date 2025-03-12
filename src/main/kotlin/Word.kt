package org.example

data class Word(
    val original: String,
    val word: String,
    var correctAnswersCount: Int = 0,
)