package diary

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl
import java.time.LocalDate

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("localhost")
        .port(24100) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    val userId = 1

    val res = diaryRemoteDataSource.getDiaries(
        userId,
        LocalDate.of(2023, 1, 1),
        LocalDate.now(),
    )

    println(res)
}
