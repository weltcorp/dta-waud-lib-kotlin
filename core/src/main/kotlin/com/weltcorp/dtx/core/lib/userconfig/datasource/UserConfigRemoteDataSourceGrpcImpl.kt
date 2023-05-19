package com.weltcorp.dtx.core.lib.userconfig.datasource

import com.weltcorp.dtx.core.lib.userconfig.UserConfigApiConfig
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPermission as UserPermissionModel
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserConfig as UserConfigModel
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserFcmToken as UserFcmTokenModel
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPushNotification as UserPushNotificationModel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import dtx.api.core.v3.project.userConfigs.UserConfigs
import dtx.api.core.v3.project.userConfigs.UserConfigsDataGrpcKt
import dtx.api.core.v3.project.userConfigs.createUserPermissionLogRequest
import dtx.api.core.v3.project.userConfigs.upsertUserConfigRequest

class UserConfigRemoteDataSourceGrpcImpl(private val config: UserConfigApiConfig) : UserConfigRemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub(getChannel())

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

    private fun stub(): UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub {
        if (_stub == null) {
            _stub = UserConfigsDataGrpcKt.UserConfigsDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun upsertUserConfig(
        userId: Int,
        projectId: Int,
        config: UserConfigModel?,
        fcmToken: UserFcmTokenModel?,
        pushNotification: UserPushNotificationModel?
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
            config?.let { this.config = it.toRemoteUserConfig() }
            fcmToken?.let { this.fcmToken = it.toRemoteUserFcmToken() }
            pushNotification?.let { this.pushNotification = it.toRemoteUserPushNotification() }
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-project-id", Metadata.ASCII_STRING_MARSHALLER), "$projectId")
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
            put(Metadata.Key.of("x-request-dtx-account-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
        }

        stub().upsertUserConfig(request, header)
    }

    override suspend fun createUserPermissionLog(
        userId: Int,
        projectId: Int,
        permission: UserPermissionModel?
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
            permission?.let {
                this.permission = UserConfigs.UserPermission.newBuilder()
                    .setPlatform(it.platform)
                    .setDeviceName(it.deviceName)
                    .setPermissions(
                        com.google.protobuf.Struct.newBuilder().putAllFields(
                            it.permissions.toValues()
                        ).build()
                    )
                    .build()
            }
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-project-id", Metadata.ASCII_STRING_MARSHALLER), "$projectId")
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
            put(Metadata.Key.of("x-request-dtx-account-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
        }

        stub().createUserPermissionLog(request, header)
    }
}

private fun UserConfigModel.toRemoteUserConfig(): UserConfigs.UserConfig {
    return UserConfigs.UserConfig.newBuilder()
        .setPlatform(this.platform)
        .setDeviceName(this.deviceName)
        .setOsVersion(this.osVersion)
        .setAppVersion(this.appVersion)
        .build()
}

private fun UserFcmTokenModel.toRemoteUserFcmToken(): UserConfigs.UserFcmToken {
    return UserConfigs.UserFcmToken.newBuilder()
        .setFcmToken(this.fcmToken)
        .setFirebaseProjectId(this.firebaseProjectId)
        .build()
}

private fun UserPushNotificationModel.toRemoteUserPushNotification(): UserConfigs.UserPushNotification {
    return UserConfigs.UserPushNotification.newBuilder()
        .setApp(this.app ?: false)
        .setSystemSettings(this.systemSettings ?: false)
        .build()
}

private fun Map<String, Any?>?.toValues(): Map<String, com.google.protobuf.Value> {
    return this?.mapValues {
        com.google.protobuf.Value.newBuilder().setStringValue(it.value.toString()).build()
    } ?: emptyMap()
}