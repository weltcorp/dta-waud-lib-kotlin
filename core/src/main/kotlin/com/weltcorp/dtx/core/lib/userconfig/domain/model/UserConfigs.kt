package com.weltcorp.dtx.core.lib.userconfig.domain.model

data class UserConfig private constructor (
    var platform: String?,
    var deviceName: String?,
    var osVersion: String?,
    var appVersion: String?,
) {
    data class Builder (
        var platform: String? = null,
        var deviceName: String? = null,
        var osVersion: String? = null,
        var appVersion: String? = null,
    ) {
        fun platform(platform: String?) = apply { this.platform = platform }
        fun deviceName(deviceName: String?) = apply { this.deviceName = deviceName }
        fun osVersion(osVersion: String?) = apply { this.osVersion = osVersion }
        fun appVersion(appVersion: String?) = apply { this.appVersion = appVersion }
        fun build() = UserConfig(platform, deviceName, osVersion, appVersion)
    }
}

data class UserFcmToken private constructor (
    var fcmToken: String?,
    var firebaseProjectId: String?,
) {
    data class Builder (
        var fcmToken: String? = null,
        var firebaseProjectId: String? = null,
    ) {
        fun fcmToken(fcmToken: String?) = apply { this.fcmToken = fcmToken }
        fun firebaseProjectId(firebaseProjectId: String?) = apply { this.firebaseProjectId = firebaseProjectId }
        fun build() = UserFcmToken(fcmToken, firebaseProjectId)
    }
}

data class UserPushNotification private constructor (
    var app: Boolean?,
    var systemSettings: Boolean?,
) {
    data class Builder (
        var app: Boolean? = null,
        var systemSettings: Boolean? = null,
    ) {
        fun app(app: Boolean?) = apply { this.app = app }
        fun systemSettings(systemSettings: Boolean?) = apply { this.systemSettings = systemSettings }
        fun build() = UserPushNotification(app, systemSettings)
    }
}

data class UserPermission private constructor(
    var platform: String?,
    var deviceName: String?,
    var permissions: Map<String, Any?>?,
) {
    data class Builder (
        var platform: String? = null,
        var deviceName: String? = null,
        var permissions: Map<String, Any?>? = null,
    ) {
        fun platform(permission: String?) = apply { this.platform = permission }
        fun deviceName(deviceName: String?) = apply { this.deviceName = deviceName }
        fun permissions(permissions: Map<String, Any?>?) = apply { this.permissions = permissions }
        fun build() = UserPermission(platform, deviceName, permissions)
    }
}
