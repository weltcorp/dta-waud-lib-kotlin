package com.weltcorp.dta.waud.lib.diary.datasource

import com.weltcorp.dta.waud.lib.diary.domain.model.Diary
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryData
import com.weltcorp.dta.waud.lib.diary.domain.model.DiaryMeta
import dta.waud.api.v1.diaries.Diaries
import io.grpc.ManagedChannel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.time.LocalDate

interface RemoteDataSource {

    suspend fun getDiaries(userId: Int, startDate: LocalDate, endDate: LocalDate): List<Diary>

    suspend fun createDiary(userId: Int, date: LocalDate, data: DiaryData)

    suspend fun updateDiary(userId: Int, answerId: Int, data: DiaryData)

    suspend fun deleteDiary(userId: Int, answerId: Int)

    /**
     * DEPRECATED
     */
    suspend fun putDiary(userId: Int, date: LocalDate, data: DiaryData)
}