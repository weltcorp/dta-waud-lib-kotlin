package diary

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .userId(1)
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    val startDate = 1679238000 // Mon Mar 20 2023 00:00:00 GMT+0900 (한국 표준시)
    val endDate = 1679497200 //Mon Mar 23 2023 00:00:00 GMT+0900 (한국 표준시)

    val res = diaryRemoteDataSource.getDiaries(startDate, endDate)

    println(res)
}
