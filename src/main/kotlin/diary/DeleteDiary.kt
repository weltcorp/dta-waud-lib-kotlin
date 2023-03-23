package diary

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl
import com.weltcorp.dta.waud.lib.diary.domain.model.*

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("localhost")
        .port(24100)
        .auth("<YOUR-TOKEN>")
        .userId(1)
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    val dateUnix = 1679238000 // Mon Mar 20 2023 00:00:00 GMT+0900 (한국 표준시)

    diaryRemoteDataSource.deleteDiary(dateUnix)

    println("Diary has been deleted.")
}