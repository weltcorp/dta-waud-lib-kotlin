package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.domain.model.Diary
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryData
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryMeta
import dta.waud.api.v1.diaries.Diaries
import io.grpc.ManagedChannel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {


    suspend fun putDiary(date: Int, data: DiaryData)

    suspend fun getDiaries(startDate: Int, endDate: Int): List<Diary>

    suspend fun deleteDiary(date: Int)
}