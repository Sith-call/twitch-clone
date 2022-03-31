package com.example.user.application.service

import com.example.user.domain.model.StreamerUser
import com.example.user.domain.model.StreamerUserStatus
import com.example.user.util.MockStreamerUserRepository
import com.example.user.util.createStreamUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("[스트리머] 수정")
class UpdateStreamerUserServiceTest {

    private lateinit var updateStreamerService: UpdateStreamerUserService
    private lateinit var mockStreamerUserRepository: MockStreamerUserRepository

    @BeforeEach
    fun beforeEach() {
        mockStreamerUserRepository = MockStreamerUserRepository()
        updateStreamerService = UpdateStreamerUserService(mockStreamerUserRepository)
    }

    @Test
    @DisplayName("스트리머 요청 리스트가 존재할 경우 스트리머 요청을 승인한다.")
    fun `pending streamer user register`() {
        // given
        saveStreamer()
        val pendingStreamers = selectPendingStreamer()

        // when
        updateStreamerService.approveStreamerUser(pendingStreamers)

        // then
        val approvedUuid = mockStreamerUserRepository.findById(pendingStreamers[0].id)

        assertAll(
            { assertThat(approvedUuid).isNotNull },
            { assertThat(approvedUuid.status).isEqualTo(StreamerUserStatus.REGISTERED) }
        )
    }

    @Test
    @DisplayName("스트리머 요청 리스트가 존재하지만 결격 사유 존재 시 스트리머 요청을 거절한다.")
    fun `pending streamer user rejected`() {
        // given
        saveStreamer()
        val pendingStreamers = selectPendingStreamer()

        // when
        updateStreamerService.rejectStreamerUser(pendingStreamers)

        // then
        val approvedUuid = mockStreamerUserRepository.findById(pendingStreamers[0].id)

        assertAll(
            { assertThat(approvedUuid).isNotNull },
            { assertThat(approvedUuid.status).isEqualTo(StreamerUserStatus.REJECTED) }
        )
    }

    @Test
    @DisplayName("스트리머의 상태를 정지 상태로 변경한다.")
    fun `update streamer status suspense`() {
        // given
        saveStreamer()
        val streamerUsers = selectPendingStreamer()
        updateStreamerService.approveStreamerUser(streamerUsers)
        val id = streamerUsers[0].id
        // when
        updateStreamerService.suspendStreamer(id)

        // then
        val findUser = mockStreamerUserRepository.findById(id)

        assertAll(
            { assertThat(findUser).isNotNull },
            { assertThat(findUser.status).isEqualTo(StreamerUserStatus.SUSPENSE) },
        )
    }

    private fun saveStreamer() {
        mockStreamerUserRepository.save(createStreamUser())
    }

    private fun selectPendingStreamer() : List<StreamerUser>{
        return mockStreamerUserRepository.findAllByStatus(StreamerUserStatus.PENDING)
    }
}
