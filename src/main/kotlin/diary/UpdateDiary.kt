package diary

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl
import com.weltcorp.dta.waud.lib.diary.domain.model.*
import java.time.LocalDate

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    val userId = 1
    val answerId = 1
    val diaryData = DiaryData.Builder()
        .alcoholCravingScore(1)
        .alcoholConsumed(true)
        .alcoholTypeAndAmount("맥주 1병")
        .sleepQualityScore(1)
        .appetiteScore(1)
        .emotionScore(1)
        .build()

    diaryRemoteDataSource.updateDiary(userId, answerId, diaryData)

    println("Diary has been updated.")
}