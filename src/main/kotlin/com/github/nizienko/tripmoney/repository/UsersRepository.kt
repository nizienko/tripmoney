package com.github.nizienko.tripmoney.repository

import com.github.nizienko.tripmoney.dto.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UsersRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun save(name: String, telegramId: Long) = transaction {
        UserEntity.new {
            this.name = name
            this.telegramId = telegramId
        }
    }

    fun find(telegramId: Long): User? = transaction {
        return@transaction Users.select { Users.telegramId eq telegramId }.singleOrNull()?.toData()
    }

    private fun ResultRow.toData(): User {
        return User(this[Users.id].value, this@toData[Users.name], this@toData[Users.telegramId])
    }

    private object Users : IntIdTable() {
        val name = varchar("name", 50).index()
        val telegramId = long("telegramId").index()
    }

    class UserEntity(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<UserEntity>(Users)

        var name by Users.name
        var telegramId by Users.telegramId
    }
}