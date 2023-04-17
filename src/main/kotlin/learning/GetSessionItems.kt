package learning

import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.datasource.LearningRemoteDataSourceGrpcImpl

suspend fun main(args: Array<String>) {
    val config = LearningApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val learningRemoteDataSource = LearningRemoteDataSourceGrpcImpl(config)

    val userId = 1

    val res = learningRemoteDataSource.getSessionItems(userId)
    println(res)
}