package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.domain.model.*
import dta.waud.api.v1.diaries.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata

class DiaryRemoteDataSourceGrpcImpl(private val config: DiaryApiConfig) : RemoteDataSource {

    private var _channel = ManagedChannelBuilder
        .forAddress(config.host, config.port)
        .usePlaintext()
        .build()

    private var _stub = DiariesDataGrpcKt.DiariesDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "${config.userId}}")
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-api")
            put(Metadata.Key.of("x-request-dtx-protocol", Metadata.ASCII_STRING_MARSHALLER), "GRPC")
            put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + config.auth)
        }
    }

    private fun getChannel(): ManagedChannel {
        if (_channel.isShutdown || _channel.isTerminated) {
            _channel = ManagedChannelBuilder
                .forAddress(config.host, config.port)
                .usePlaintext()
                .build()
        }
        return _channel
    }

    private fun stub(): DiariesDataGrpcKt.DiariesDataCoroutineStub {
        if (_stub == null) {
            _stub = DiariesDataGrpcKt.DiariesDataCoroutineStub(getChannel())
        }
        return _stub
    }

    override suspend fun putDiary(date: Int, data: DiaryData) {

        if (config.userId == 0) {
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
            this.userId = config.userId
            this.date = date.toLong()
            this.diary = diaryData
        }

        val header = getHeader()

        stub().putDiary(request, header)
    }

    override suspend fun getDiaries(startDate: Int, endDate: Int): List<Diary> {

        if (config.userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val request = getDiariesRequest{
            this.userId = config.userId
            this.startDate = startDate.toLong()
            this.endDate = endDate.toLong()
        }
        val header = getHeader()

        val resp = stub().getDiaries(request, header)

        println(resp)
        println("resp.diariesList")
        println(resp.diariesList)

        val diaries = resp.diariesList.map { diary ->
            Diary().apply {
                meta = DiaryMeta.Builder()
                    .dateUnix(diary.meta.dateUnix)
                    .dateString(diary.meta.dateString)
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

        return diaries
    }

    override suspend fun deleteDiary(date: Int) {

        if (config.userId == 0) {
            throw IllegalArgumentException("userId is not set")
        }

        val request = deleteDiaryRequest {
            this.userId = config.userId
            this.date = date.toLong()
        }

        val header = getHeader()

        stub().deleteDiary(request, header)
    }
}

