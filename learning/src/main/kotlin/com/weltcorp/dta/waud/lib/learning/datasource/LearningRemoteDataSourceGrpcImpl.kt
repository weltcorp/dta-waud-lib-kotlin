package com.weltcorp.dta.waud.lib.learning.datasource

import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.domain.model.SessionItem

class LearningRemoteDataSourceGrpcImpl(private val config: LearningApiConfig): LearningRemoteDataSource {

    override suspend fun getSessionItems(userId: Int): List<SessionItem> {
        // TODO: Implement this
        return listOf(
            SessionItem(1, "나는 고위험 음주 상태일까", 1, 1, 1, "1장 고위험 음주란?", 5),
            SessionItem(2, "4주 동안의 약속, 단주", 2, 1, 2, "1장 고위험 음주란?", 5),
            SessionItem(3, "술과 수면", 3, 1, 3, "2장 술을 끊으면 어떤 변화가?", 5),
            SessionItem(4, "술과 식욕", 4, 1, 4, "2장 술을 끊으면 어떤 변화가?", 5),
            SessionItem(5, "술과 감정", 5, 1, 5, "2장 술을 끊으면 어떤 변화가?", 5),
            SessionItem(6, "자극", 6, 1, 6, "3장 파블로프의 개", 5),
            SessionItem(7, "갈망", 7, 1, 7, "3장 파블로프의 개", 5),
        )
    }
}