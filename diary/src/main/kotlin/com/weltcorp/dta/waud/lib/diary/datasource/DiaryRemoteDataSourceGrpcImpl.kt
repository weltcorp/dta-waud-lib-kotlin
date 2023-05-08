package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.domain.model.Diary
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryData
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryMeta
import dta.waud.api.v1.diaries.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DiaryRemoteDataSourceGrpcImpl(private val config: DiaryApiConfig) : RemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = DiariesDataGrpcKt.DiariesDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-api")
            put(Metadata.Key.of("x-request-dtx-protocol", Metadata.ASCII_STRING_MARSHALLER), "GRPC")
            put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + config.auth)
        }
    }

    private fun getChannel(): ManagedChannel {
        if (_channel.isShutdown || _channel.isTerminated) {
            _channel = getMangedChannel()
        }
        return _channel
    }

    private fun getMangedChannel(): ManagedChannel {
        val channel = ManagedChannelBuilder
            .forTarget("${config.host}${if (config.port != null) ":${config.port}" else ""}")
        if (config.port != null) {
            channel.usePlaintext()
        }
        return channel.build()
    }

    private fun stub(): DiariesDataGrpcKt.DiariesDataCoroutineStub {
        if (_stub == null) {
            _stub = DiariesDataGrpcKt.DiariesDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun getDiaries(userId: Int, startDate: LocalDate, endDate: LocalDate): List<Diary> {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val request = getDiariesRequest{
            this.userId = userId
            this.startDate =  startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // startDate.toLong()
            this.endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // endDate.toLong()
        }
        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}}")
        }

        val resp = stub().getDiaries(request, header)
        println(resp)

        val tempDiaryMap = mutableMapOf<LocalDate, Diary>()
        var tempStartDate = LocalDate.of(startDate.year, startDate.month, startDate.dayOfMonth)
        // Fill tempDiaryMap with empty diary
        while (tempStartDate < endDate) {
            tempDiaryMap[tempStartDate] = Diary().initEmptyDiary(tempStartDate)
            tempStartDate = tempStartDate.plusDays(1)
        }

        resp.diariesList.map { diary ->
            // Replace tempDiaryMap with diary data from server
            tempDiaryMap[LocalDate.ofEpochDay(diary.meta.dateUnix.toLong())] =
                Diary().apply {
                    meta = DiaryMeta.Builder()
                        .answerId(diary.meta.answerId)
                        .dateUnix(diary.meta.dateUnix)
                        .dateString(diary.meta.dateString)
                        .description("기록을 작성하거나 수정하세요")
                        .isCompleted(true)
                        .build()

                    data = DiaryData.Builder()
                        .alcoholCravingScore(diary.data.alcoholCravingScore)
                        .alcoholConsumed(diary.data.alcoholConsumed)
                        .alcoholTypeAndAmount(diary.data.alcoholTypeAndAmount)
                        .sleepQualityScore(diary.data.sleepQualityScore)
                        .appetiteScore(diary.data.appetiteScore)
                        .emotionScore(diary.data.emotionScore)
                        .build()
                }
        }
        return tempDiaryMap.values.toList()
    }

    override suspend fun createDiary(userId: Int, date: LocalDate, data: DiaryData) {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val diaryData = diaryData {
            data.alcoholCravingScore?.let { this.alcoholCravingScore = it }
            data.alcoholConsumed?.let { this.alcoholConsumed = it }
            data.alcoholTypeAndAmount?.let { this.alcoholTypeAndAmount = it }
            data.sleepQualityScore?.let { this.sleepQualityScore = it }
            data.appetiteScore?.let { this.appetiteScore = it }
            data.emotionScore?.let { this.emotionScore = it }
        }

        val request = createDiaryRequest {
            this.userId = userId
            this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            this.diary = diaryData
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}}")
        }

        stub().createDiary(request, header)
    }

    override suspend fun updateDiary(userId: Int, answerId: Int, data: DiaryData) {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }
        if (answerId == 0) {
            throw IllegalArgumentException("answerId is not set")
        }

        val diaryData = diaryData {
            data.alcoholCravingScore?.let { this.alcoholCravingScore = it }
            data.alcoholConsumed?.let { this.alcoholConsumed = it }
            data.alcoholTypeAndAmount?.let { this.alcoholTypeAndAmount = it }
            data.sleepQualityScore?.let { this.sleepQualityScore = it }
            data.appetiteScore?.let { this.appetiteScore = it }
            data.emotionScore?.let { this.emotionScore = it }
        }

        val request = updateDiaryRequest {
            this.userId = userId
            this.answerId = answerId
            this.diary = diaryData
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}}")
        }

        stub().updateDiary(request, header)
    }

    override suspend fun deleteDiary(userId: Int, answerId: Int) {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val request = deleteDiaryRequest {
            this.userId = userId
            this.answerId = answerId
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}}")
        }

        stub().deleteDiary(request, header)
    }

    override suspend fun putDiary(userId: Int, date: LocalDate, data: DiaryData) {
        if (userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val diaryData = diaryData {
            data.alcoholCravingScore?.let { this.alcoholCravingScore = it }
            data.alcoholConsumed?.let { this.alcoholConsumed = it }
            data.alcoholTypeAndAmount?.let { this.alcoholTypeAndAmount = it }
            data.sleepQualityScore?.let { this.sleepQualityScore = it }
            data.appetiteScore?.let { this.appetiteScore = it }
            data.emotionScore?.let { this.emotionScore = it }
        }

        val request = putDiaryRequest {
            this.userId = userId
            this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            this.diary = diaryData
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${userId}}")
        }

        stub().putDiary(request, header)
    }
}

private fun Diary.initEmptyDiary(localDate: LocalDate): Diary {
    return Diary().apply {
        meta = DiaryMeta.Builder()
            .dateUnix(localDate.toEpochDay().toInt())
            .dateString(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .build()

        data = DiaryData.Builder().build()
    }
}
