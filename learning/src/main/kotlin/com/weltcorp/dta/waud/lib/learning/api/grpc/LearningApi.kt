package com.weltcorp.dta.waud.lib.learning.api.grpc

import io.reactivex.rxjava3.core.Completable

interface LearningApi {

    fun createLearning(
    ): Completable
}