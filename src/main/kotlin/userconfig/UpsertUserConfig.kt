package userconfig

import com.weltcorp.dtx.core.lib.userconfig.UserConfigApiConfig
import com.weltcorp.dtx.core.lib.userconfig.datasource.UserConfigRemoteDataSourceGrpcImpl
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserConfig
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserFcmToken
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPushNotification

class UpsertUserConfig {

    suspend fun main(args: Array<String>) {
        val config = UserConfigApiConfig.Builder()
            .host("localhost")
            .port(24100) // The port can be removed, if you don't need.
            .auth("<YOUR-TOKEN>")
            .build()

        val userConfigDataSource = UserConfigRemoteDataSourceGrpcImpl(config)

        val userId = -1
        val projectId = -1

        val userConfig = UserConfig.Builder()
            .platform("android")
            .deviceName("<DEVICE-NAME>")
            .osVersion("<OS-VERSION>")
            .appVersion("<APP-VERSION>")
            .build()

        val userFcmToken = UserFcmToken.Builder()
            .fcmToken("<FCM-TOKEN>")
            .firebaseProjectId("<FIREBASE-PROJECT-ID>")
            .build()

        val userPushNotification = UserPushNotification.Builder()
            .app(false)
            .systemSettings(false)
            .build()

        userConfigDataSource.upsertUserConfig(
            userId = userId,
            projectId = projectId,
            config = userConfig, // It can be null.
            fcmToken = userFcmToken, // It can be null.
            pushNotification = userPushNotification, // It can be null.
        )
    }
}