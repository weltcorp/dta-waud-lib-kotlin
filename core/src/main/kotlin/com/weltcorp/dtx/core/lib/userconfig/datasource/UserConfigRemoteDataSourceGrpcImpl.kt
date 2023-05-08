package com.weltcorp.dtx.core.lib.userconfig.datasource

import com.weltcorp.dtx.core.lib.userconfig.UserConfigApiConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import userConfigs.UserConfigs
import userConfigs.UserConfigsDataGrpcKt
import userConfigs.createUserPermissionLogRequest
import userConfigs.upsertUserConfigRequest

class UserConfigRemoteDataSourceGrpcImpl(private val config: UserConfigApiConfig) : UserConfigRemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dtx-api-core")
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

    private fun stub(): UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub {
        if (_stub == null) {
            _stub = UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun upsertUserConfig(
        userId: Int,
        projectId: Int,
        config: UserConfigs.UserConfig,
        fcmToken: UserConfigs.UserFcmToken,
        pushNotification: UserConfigs.UserPushNotification
    ) {
        if (userId < 1) {
            throw IllegalArgumentException("userId is not set")
        }
        if (projectId < 1) {
            throw IllegalArgumentException("projectId is not set")
        }

        val request = upsertUserConfigRequest {
            this.userId = userId
            this.projectId = projectId
            this.config = config
            this.fcmToken = fcmToken
            this.pushNotification = pushNotification
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}")
        }

        stub().upsertUserConfig(request, header)
    }

    override suspend fun createUserPermissionLog(
        userId: Int,
        projectId: Int,
        permission: UserConfigs.UserPermission
    ) {
        if (userId < 1) {
            throw IllegalArgumentException("userId is not set")
        }
        if (projectId < 1) {
            throw IllegalArgumentException("projectId is not set")
        }

        val request = createUserPermissionLogRequest {
            this.userId = userId
            this.projectId = projectId
            this.permission = permission
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}")
        }

        stub().createUserPermissionLog(request, header)
    }
}