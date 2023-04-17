package com.weltcorp.dta.waud.lib.learning.datasource

import com.weltcorp.dta.waud.lib.learning.domain.model.SessionItem

interface LearningRemoteDataSource {

    suspend fun getSessionItems(userId: Int): List<SessionItem>
}