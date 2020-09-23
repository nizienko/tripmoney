package com.github.nizienko.tripmoney.bot

import com.elbekD.bot.Bot
import com.elbekD.bot.feature.chain.chain
import com.github.nizienko.tripmoney.repository.UsersRepository
import org.kodein.di.DI
import org.kodein.di.instance

class TelegramBot(di: DI) {
    private val token by di.instance<String>("token")
    private val userName by di.instance<String>("userName")

    private val usersRepository by di.instance<UsersRepository>()

    private val bot = Bot.createPolling(userName, token).apply {
        onCommand("/start") { msg, _ ->
            println("message received")
            val userId = msg.from?.id?.toLong()
            if (userId != null) {
                val user = usersRepository.find(userId)?.apply { sendMessage(msg.chat.id, "Hello, I know you, $name") }
                println(user)
                if (user == null) {
                    val name = msg.from?.username ?: msg.from?.first_name ?: msg.from?.id?.toString() ?: "unknown"
                    usersRepository.save(name, userId)
                    sendMessage(msg.chat.id, "Nice to meet you, will remember you name, $name")
                }
            }
        }
    }

    fun start() = bot.start()
}