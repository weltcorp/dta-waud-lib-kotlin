package com.weltcorp.dta.waud.lib.learning.api.http

import io.reactivex.rxjava3.core.Completable

interface ProjectApi {

    fun createProject(
    ): Completable
}