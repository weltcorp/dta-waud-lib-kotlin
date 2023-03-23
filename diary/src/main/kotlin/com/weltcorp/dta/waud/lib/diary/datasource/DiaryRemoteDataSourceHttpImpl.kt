package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.domain.model.Diary
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryData
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryMeta
import dta.waud.api.v1.diaries.Diaries
import dta.waud.api.v1.diaries.DiariesDataGrpcKt
import dta.waud.api.v1.diaries.putDiaryRequest
import dta.waud.api.v1.diaries.diaryData
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class DiaryRemoteDataSourceHttpImpl(
    val config: DiaryApiConfig
) : RemoteDataSource {

    override suspend fun putDiary(date: Int, data: DiaryData) {
        //Todo: Implement this
    }

    override suspend fun deleteDiary(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getDiaries(startDate: Int, endDate: Int): List<Diary> {
        TODO("Not yet implemented")
    }
}