package com.weltcorp.dta.waud.lib.learning.datasource

import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.domain.model.SessionItem
import dta.waud.api.v1.learning.LearningDataGrpcKt
import dta.waud.api.v1.learning.getUserSessionItemsRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata

class LearningRemoteDataSourceGrpcImpl(private val config: LearningApiConfig): LearningRemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = LearningDataGrpcKt.LearningDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-api")
            put(Metadata.Key.of("x-request-dtx-protocol", Metadata.ASCII_STRING_MARSHALLER), "GRPC")
            put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + config.auth)
        }
    }

    private fun getChannel(): ManagedChannel {
        if (_channel.isShutdown || _channel.isTerminated) {
            _channel = getMangedChannel()
        }
        return _channel
    }

    private fun getMangedChannel(): ManagedChannel {
        val channel = ManagedChannelBuilder
            .forTarget("${config.host}${if (config.port != null) ":${config.port}" else ""}")
        if (config.port != null) {
            channel.usePlaintext()
        }
        return channel.build()
    }

    private fun stub(): LearningDataGrpcKt.LearningDataCoroutineStub {
        if (_stub == null) {
            _stub = LearningDataGrpcKt.LearningDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun getSessionItems(userId: Int): List<SessionItem> {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val request = getUserSessionItemsRequest{
            this.userId = userId
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}")
        }

        val resp = stub().getUserSessionItems(request, header)

        val sessionItems = resp.dataList.map { sessionItem ->
            SessionItem(
                id = sessionItem.id,
                name = sessionItem.name,
                order = sessionItem.order,
                typeId = sessionItem.typeId,
                sessionId = sessionItem.sessionId,
                sessionName = sessionItem.sessionName,
                userLearningStatusId = sessionItem.userLearningStatusId
            )
        }
        return sessionItems
    }
}