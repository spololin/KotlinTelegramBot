package org.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Update(
    @SerialName("update_id")
    val updateId: Long,
    @SerialName("message")
    val message: Message? = null,
    @SerialName("callback_query")
    val callbackQuery: CallbackQuery? = null,
)

@Serializable
data class Response(
    @SerialName("result")
    val result: List<Update>,
)

@Serializable
data class Message(
    @SerialName("text")
    val text: String? = null,
    @SerialName("chat")
    val chat: Chat,
)

@Serializable
data class CallbackQuery(
    @SerialName("data")
    val data: String,
    @SerialName("message")
    val message: Message? = null,
)

@Serializable
data class Chat(
    @SerialName("id")
    val id: Long,
)


fun main(args: Array<String>) {
    val botToken = args[0]
    val telegramService = TelegramBotService(botToken)
    val trainer = LearnWordTrainer()
    var lastUpdateId = 0L

    while (true) {
        Thread.sleep(2000)
        val updates = telegramService.getUpdates(lastUpdateId).result
        if (updates.isEmpty()) continue
        val sortedUpdates = updates.sortedBy { it.updateId }
        sortedUpdates.forEach { handleUpdate(it, trainer, telegramService) }
        lastUpdateId = sortedUpdates.last().updateId + 1
    }
}

fun handleUpdate(update: Update, trainer: LearnWordTrainer, telegramService: TelegramBotService) {
    val message = update.message?.text
    val chatId = update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id
    val data = update.callbackQuery?.data

    if (message == "hello" && chatId != null)
        telegramService.sendMessage(chatId, "Hello!")

    if (message == "menu" && chatId != null)
        telegramService.sendMenu(chatId)

    if (data == STATISTICS_CLICKED && chatId != null) {
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

    if (data == LEARN_WORDS_CLICKED && chatId != null)
        checkNextQuestionAndSend(trainer, telegramService, chatId)

    if (data?.startsWith(CALLBACK_DATA_ANSWER_PREFIX) == true && chatId != null) {
        val answerIdx = data.substringAfter("_").toInt()

        if (trainer.checkAnswer(answerIdx)) {
            telegramService.sendMessage(
                chatId,
                "Правильно!"
            )
        } else {
            telegramService.sendMessage(
                chatId,
                "Неправильно! ${trainer.question?.correctAnswer?.original} " +
                        "- это ${trainer.question?.correctAnswer?.translate}"
            )
        }

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
