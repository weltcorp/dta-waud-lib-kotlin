package com.weltcorp.dtx.core.lib.userconfig.datasource

import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserConfig
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserFcmToken
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPushNotification
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPermission

interface UserConfigRemoteDataSource {

    suspend fun upsertUserConfig(
        userId: Int,
        projectId: Int,
        config: UserConfig?,
        fcmToken: UserFcmToken?,
        pushNotification: UserPushNotification?,
    )

    suspend fun createUserPermissionLog(
        userId: Int,
        projectId: Int,
        permission: UserPermission?,
    )
}