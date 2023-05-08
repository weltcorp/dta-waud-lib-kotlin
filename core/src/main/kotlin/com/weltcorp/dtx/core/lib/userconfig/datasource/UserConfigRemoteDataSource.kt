package com.weltcorp.dtx.core.lib.userconfig.datasource

import userConfigs.UserConfigs

interface UserConfigRemoteDataSource {

    suspend fun upsertUserConfig(
        userId: Int,
        projectId: Int,
        config: UserConfigs.UserConfig,
        fcmToken: UserConfigs.UserFcmToken,
        pushNotification: UserConfigs.UserPushNotification,
    )

    suspend fun createUserPermissionLog(
        userId: Int,
        projectId: Int,
        permission: UserConfigs.UserPermission,
    )
}