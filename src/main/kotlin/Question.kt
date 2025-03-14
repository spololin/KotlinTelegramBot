package org.example

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

fun Question.asConsoleString(): String {
    val variants = this.variants.mapIndexed { index, word ->
        " ${index + 1} - ${word.translate}"
    }.joinToString("\n")

    return "${this.correctAnswer.original}\n$variants\n-------\n 0 - выйти в меню"
}