package com.weltcorp.dta.waud.lib.diary.api.grpc

import io.reactivex.rxjava3.core.Completable

interface DiaryApi {

    fun createDiary(
    ): Completable
}