package org.example

fun main() {
    val trainer = LearnWordTrainer()

    while (true) {
        println(
            """
            Меню: 
            1 – Учить слова
            2 – Статистика
            0 – Выход
        """.trimIndent()
        )

        val action = readln().trim()

        when (action) {
            "1" -> {
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова в словаре выучены")
                        break
                    }
                    println(question.asConsoleString())

                    val answer = readln().toIntOrNull()
                    if (answer == 0) break

                    if (trainer.checkAnswer(answer?.minus(1))) {
                        println("Правильно!\n")
                    } else {
                        println("Неправильно! ${question.correctAnswer.original} - это ${question.correctAnswer.translate}")
                    }
                }
            }
            "2" -> {
                val statistics = trainer.calculateStatistics()
                println(
                    String.format(
                        "Выучено %d из %d | %d",
                        statistics.correctAnswersCount, statistics.totalCount, statistics.percent
                    )
                )
            }

            "0" -> break
            else -> println("Введите 1, 2 или 0")
        }
    }
}