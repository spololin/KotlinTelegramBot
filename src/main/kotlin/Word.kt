package org.example

data class Word(
    val original: String,
    val word: String,
    val correctAnswersCount: Int = 0,
)