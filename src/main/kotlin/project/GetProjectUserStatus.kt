package project

import com.weltcorp.dtx.core.lib.project.ProjectApiConfig
import com.weltcorp.dtx.core.lib.project.datasource.ProjectRemoteDataSourceGrpcImpl

suspend fun main(args: Array<String>) {
    val config = ProjectApiConfig.Builder()
        .host("dtx-api-dev.weltcorp.com")
//        .port(443) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val projectRemoteDataSource = ProjectRemoteDataSourceGrpcImpl(config)

    val userId = 17933
    val res = projectRemoteDataSource.getProjectUserStatus(userId)
    println(res)
}