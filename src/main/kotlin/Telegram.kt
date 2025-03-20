package org.example

fun main(args: Array<String>) {
    val botToken = args[0]
    val telegramService = TelegramBotService(botToken)
    var updateId: Int? = 0
    val updateIdRegex: Regex = "\"update_id\":(\\d+),".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+)\"".toRegex()
    val chatIdRegex: Regex = "\"chat\":.\"id\":(\\d+),".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramService.getUpdates(updateId)
        println(updates)

        val updateIdMatchResult: MatchResult? = updateIdRegex.find(updates)
        val updateIdGroups = updateIdMatchResult?.groups
        updateId = updateIdGroups?.get(1)?.value?.toIntOrNull()?.plus(1) ?: continue

        val textMatchResult: MatchResult? = messageTextRegex.find(updates)
        val textGroups = textMatchResult?.groups
        val text = textGroups?.get(1)?.value

        val chatIdMatchResult: MatchResult? = chatIdRegex.find(updates)
        val chatIdGroups = chatIdMatchResult?.groups
        val chatId = chatIdGroups?.get(1)?.value?.toIntOrNull() ?: continue

        if (text.equals("hello", ignoreCase = true)) telegramService.sendMessage(chatId, "Hello!")
    }
}
