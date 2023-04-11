package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.domain.model.Diary
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryData
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryMeta
import dta.waud.api.v1.diaries.Diaries
import io.grpc.ManagedChannel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {


    suspend fun putDiary(userId: Int, date: Int, data: DiaryData)

    suspend fun getDiaries(userId: Int, startDate: Int, endDate: Int): List<Diary>

    suspend fun deleteDiary(userId: Int, date: Int)
}