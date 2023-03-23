package com.weltcorp.dta.waud.lib.diary.api.http

import io.reactivex.rxjava3.core.Completable

interface DiaryApi {

    fun createDiary(
    ): Completable
}