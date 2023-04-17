package com.weltcorp.dta.waud.lib.learning.datasource

import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.domain.model.SessionItem

class LearningRemoteDataSourceHttpImpl(private val config: LearningApiConfig): LearningRemoteDataSource {

    override suspend fun getSessionItems(userId: Int): List<SessionItem> {
        TODO("Not yet implemented")
    }
}