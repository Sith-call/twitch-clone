package com.example.user.application.service

import com.example.user.domain.model.StreamerUser
import com.example.user.domain.model.StreamerUserStatus
import com.example.user.domain.model.User
import com.example.user.util.MockStreamerUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("[스트리머] 조회")
class SelectStreamerUserServiceTest {

    private lateinit var selectStreamerService: SelectStreamerUserService
    private lateinit var mockStreamerUserRepository: MockStreamerUserRepository

    @BeforeEach
    fun beforeEach() {
        mockStreamerUserRepository = MockStreamerUserRepository()
        selectStreamerService = SelectStreamerUserService(mockStreamerUserRepository)
    }

    @Test
    @DisplayName("스트리머 요청 승인을 위하여 스트리머 신청한 리스트를 가져온다.")
    fun `select pending streamer user list`() {
        // given
        createStreamer()

        // when
        val pendingStreamerUsers = selectStreamerService.selectPendingStreamers()

        // then
        Assertions.assertAll(
            { assertThat(pendingStreamerUsers).isNotEmpty },
            { assertThat(pendingStreamerUsers[0].status).isEqualTo(StreamerUserStatus.PENDING) }
        )
    }

    @Test
    @DisplayName("스트리머 요청이 존재하지 않을 경우 빈 리스트를 반환한다.")
    fun `select pending streamer user list if not exists return empty list`() {
        // when
        val pendingStreamerUsers = selectStreamerService.selectPendingStreamers()

        // then
        assertThat(pendingStreamerUsers).isEmpty()
    }

    private fun createUser() = User(email = "test@Test.com", nickname = "test", password = "password01")

    private fun createStreamer() {
        val user = createUser()
        mockStreamerUserRepository.save(StreamerUser(user = user, streamerNickname = "streamer"))
    }
}
