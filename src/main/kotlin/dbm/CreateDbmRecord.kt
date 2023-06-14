package dbm

import com.weltcorp.dta.waud.lib.dbm.DbmApiConfig
import com.weltcorp.dta.waud.lib.dbm.datasource.DbmRemoteDataSourceGrpcImpl
import com.weltcorp.dta.waud.lib.dbm.domain.model.KindInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.SourceInfo

suspend fun main(args: Array<String>) {
    val config = DbmApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val sourceInfo = SourceInfo(
        value = "Rano's iPhone",
        deviceName = "iPhone",
        hardwareName = "iPhone 12 Pro Max",
        hardwareVersion = "14.8",
        softwareVersion = "14.8",
        bundleIdentifier = "com.google.HealthConnect",
    )

    val kindInfoList = listOf(
        KindInfo(
            value = "steps",
            startTime = "1686638280",
            endTime = "1686638280",
        )
    )

    val dbmRemoteDataSource = DbmRemoteDataSourceGrpcImpl(config)
    dbmRemoteDataSource.createDbmRecord(
        osEnv = "dev",
        projectId = 9,
        userId = 17520,
        categoryValue = "steps",
        sourceInfo = sourceInfo,
        kindInfoList = kindInfoList,
    )
}