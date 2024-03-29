package com.weltcorp.dtx.core.lib.project.datasource

import com.weltcorp.dtx.core.lib.project.ProjectApiConfig
import com.weltcorp.dtx.core.lib.project.domain.model.ProjectUserStatus
import dtx.api.core.v3.project.users.ProjectUsersDataGrpcKt
import dtx.api.core.v3.project.users.getUserStatusByProjectIdRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata

class ProjectRemoteDataSourceGrpcImpl(private val config: ProjectApiConfig) : ProjectRemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = ProjectUsersDataGrpcKt.ProjectUsersDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-src-account-type", Metadata.ASCII_STRING_MARSHALLER), "0")
            put(Metadata.Key.of("x-request-dtx-src-domain-id", Metadata.ASCII_STRING_MARSHALLER), "100")
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-protocol", Metadata.ASCII_STRING_MARSHALLER), "grpc")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dtx-api-core")
            put(Metadata.Key.of("x-request-dtx-dst-service-version", Metadata.ASCII_STRING_MARSHALLER), "v3")
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

    private fun stub(): ProjectUsersDataGrpcKt.ProjectUsersDataCoroutineStub {
        if (_stub == null) {
            _stub = ProjectUsersDataGrpcKt.ProjectUsersDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun getProjectUserStatus(userId: Int, projectId: Int): ProjectUserStatus {
        if (userId < 1) {
            throw IllegalArgumentException("userId is not set")
        }

        if (projectId < 1) {
            throw IllegalArgumentException("projectId is not set")
        }

        val request = getUserStatusByProjectIdRequest {
            this.userId = userId
            this.projectId = projectId
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}")
        }

        try {
            stub().getUserStatusByProjectId(request, header)
        } catch (e: Exception) {
            println(e)
        }
        val resp = stub().getUserStatusByProjectId(request, header)

        return when(resp.status) {
            ProjectUserStatus.NONE.ordinal -> ProjectUserStatus.NONE
            ProjectUserStatus.ENABLED.ordinal -> ProjectUserStatus.ENABLED
            ProjectUserStatus.DISABLED.ordinal -> ProjectUserStatus.DISABLED
            ProjectUserStatus.DELETED.ordinal -> ProjectUserStatus.DELETED
            ProjectUserStatus.BLOCKED.ordinal -> ProjectUserStatus.BLOCKED
            ProjectUserStatus.PAYMENT_REQUIRED.ordinal -> ProjectUserStatus.PAYMENT_REQUIRED
            ProjectUserStatus.EXPIRED.ordinal -> ProjectUserStatus.EXPIRED
            ProjectUserStatus.DORMANT.ordinal -> ProjectUserStatus.DORMANT
            ProjectUserStatus.IN_POST_ASSESSMENT.ordinal -> ProjectUserStatus.IN_POST_ASSESSMENT
            ProjectUserStatus.DROPOUT.ordinal -> ProjectUserStatus.DROPOUT
            else -> ProjectUserStatus.NONE
        }
    }
}