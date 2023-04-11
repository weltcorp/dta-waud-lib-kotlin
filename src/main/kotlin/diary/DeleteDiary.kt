package diary

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl
import com.weltcorp.dta.waud.lib.diary.domain.model.*

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    val userId = 1
    val dateUnix = 1679238000 // Mon Mar 20 2023 00:00:00 GMT+0900 (한국 표준시)

    diaryRemoteDataSource.deleteDiary(userId, dateUnix)

    println("Diary has been deleted.")
}