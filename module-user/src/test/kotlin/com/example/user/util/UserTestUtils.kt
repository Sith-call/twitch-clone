package com.example.user.util

import com.example.user.domain.model.NormalUser
import com.example.user.domain.model.UserStatus
import com.github.javafaker.Faker
import java.util.*

fun randomUser(
    id: UUID? = null,
    email: String? = null,
    password: String? = null,
    nickname: String? = null,
    status: UserStatus? = null
): NormalUser = with(Faker()) {
    NormalUser(
        id = id ?: UUID.randomUUID(),
        email = email ?: internet().emailAddress(),
        password = password ?: lorem().characters(10),
        nickname = nickname ?: name().name(),
        status = status ?: UserStatus.REGISTERED
    )
}
