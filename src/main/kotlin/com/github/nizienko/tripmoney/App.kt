package com.github.nizienko.tripmoney

import com.github.nizienko.tripmoney.bot.TelegramBot
import com.github.nizienko.tripmoney.checker.Checker
import com.github.nizienko.tripmoney.repository.UsersRepository
import org.jetbrains.exposed.sql.Database
import org.kodein.di.*

fun main() {
    App().run()
}

class App : DIAware {
    override val di = DI {
        constant("token") with System.getProperty("token")
        constant("userName") with "Test Bot"

        bind<TelegramBot>() with singleton { TelegramBot(di) }
        bind<Checker>() with singleton { Checker(di) }

        // database
        bind<UsersRepository>() with singleton { UsersRepository() }
    }

    fun run() {
        Database.connect("jdbc:h2:file:./telegram-bot", driver = "org.h2.Driver")

        val telegram by instance<TelegramBot>()
        telegram.start()
    }
}