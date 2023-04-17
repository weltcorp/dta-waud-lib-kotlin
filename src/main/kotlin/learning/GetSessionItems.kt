package learning

import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.datasource.LearningRemoteDataSourceGrpcImpl

suspend fun main(args: Array<String>) {
    val config = LearningApiConfig.Builder()
        .host("dta-waud-api-dev.weltcorp.com")
        .port(443) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val learningRemoteDataSource = LearningRemoteDataSourceGrpcImpl(config)

    val userId = 100
    val res = learningRemoteDataSource.getSessionItems(userId)
    println(res)
}