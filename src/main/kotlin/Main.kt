package org.example

fun main() {
    val dictionary = Dictionary()
    val dictionaryWords = dictionary.getListWords()

    while (true) {
        println("""
            Меню: 
            1 – Учить слова
            2 – Статистика
            0 – Выход
        """.trimIndent())

        val action = readln().trim()

        when (action) {
            "1" -> println("Учить слова")
            "2" -> println("Статистика")
            "0" -> break
            else -> println("Введите 1, 2 или 0")
        }
    }
}