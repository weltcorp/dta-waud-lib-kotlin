package com.weltcorp.dtx.core.lib.userconfig.api.grpc

import io.reactivex.rxjava3.core.Completable

interface UserConfigApi {

    fun createUserConfig(
    ): Completable
}