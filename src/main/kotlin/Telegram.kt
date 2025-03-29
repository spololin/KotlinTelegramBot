package org.example

fun main(args: Array<String>) {
    val botToken = args[0]
    val telegramService = TelegramBotService(botToken)
    val trainer = LearnWordTrainer()
    var updateId = 0

    val updateIdRegex: Regex = "\"update_id\":(\\d+),".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+)\"".toRegex()
    val chatIdRegex: Regex = "\"chat\":.\"id\":(\\d+),".toRegex()
    val dataRegex: Regex = "\"data\":\"(.+?)\"".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramService.getUpdates(updateId)
        println(updates)

        updateId = updateIdRegex.find(updates)?.groups?.get(1)?.value?.toIntOrNull()?.plus(1) ?: continue

        val message = messageTextRegex.find(updates)?.groups?.get(1)?.value
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toLongOrNull() ?: continue
        val data = dataRegex.find(updates)?.groups?.get(1)?.value

        if (message.equals("hello", ignoreCase = true))
            telegramService.sendMessage(chatId, "Hello!")

        if (message.equals("menu", ignoreCase = true))
            telegramService.sendMenu(chatId)

        if (data.equals("statistics_clicked", ignoreCase = true)) {
            val statistics = trainer.calculateStatistics()
            telegramService.sendMessage(
                chatId, String.format(
                    "Выучено %d из %d слов | %d%%",
                    statistics.correctAnswersCount,
                    statistics.totalCount,
                    statistics.percent
                )
            )
        }

        if (data.equals("learn_words_clicked", ignoreCase = true))
            checkNextQuestionAndSend(trainer, telegramService, chatId)
    }
}

fun checkNextQuestionAndSend(trainer: LearnWordTrainer, service: TelegramBotService, chatId: Long) {
    val question = trainer.getNextQuestion()

    if (question == null)
        service.sendMessage(chatId, "Все слова в словаре выучены")
    else
        service.sendQuestion(chatId, question)
}
