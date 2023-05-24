package userconfig

import com.weltcorp.dtx.core.lib.userconfig.UserConfigApiConfig
import com.weltcorp.dtx.core.lib.userconfig.datasource.UserConfigRemoteDataSourceGrpcImpl
import com.weltcorp.dtx.core.lib.userconfig.domain.model.UserPermission

suspend fun main(args: Array<String>) {
    val config = UserConfigApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val userConfigDataSource = UserConfigRemoteDataSourceGrpcImpl(config)

    val userId = 1
    val projectId = 1

    val userPermission = UserPermission.Builder()
        .platform("android")
        .deviceName("samsung")
        .permissions(mapOf(
            "android.permission.health.READ_HEART_RATE" to false,
        ))
        .build()

    userConfigDataSource.createUserPermissionLog(
        userId,
        projectId,
        userPermission,
    )
}