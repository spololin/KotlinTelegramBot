package org.example

fun main() {
    val dictionary = Dictionary()

    while (true) {
        println("""
            Меню: 
            1 – Учить слова
            2 – Статистика
            0 – Выход
        """.trimIndent())

        val action = readln().trim()

        when (action) {
            "1" -> dictionary.processLearning()
            "2" -> dictionary.calculateStatistics()
            "0" -> break
            else -> println("Введите 1, 2 или 0")
        }
    }
}