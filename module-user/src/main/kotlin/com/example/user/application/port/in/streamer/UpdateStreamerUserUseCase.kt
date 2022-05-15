package com.example.user.application.port.`in`.streamer

import com.example.user.application.port.out.StreamerUserRepository
import com.example.user.application.service.streamer.UpdateStreamerUserService
import java.util.*

interface UpdateStreamerUserUseCase {

    fun updateStreamerNickname(id: UUID, updateNickname: String)

    companion object {
        fun create(streamerUserRepository: StreamerUserRepository): UpdateStreamerUserUseCase = UpdateStreamerUserService(streamerUserRepository)
    }
}
