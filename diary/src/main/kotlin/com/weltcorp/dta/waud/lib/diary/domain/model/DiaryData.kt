package com.weltcorp.dta.waud.lib.diary.domain.model

data class DiaryData private constructor(
    var alcoholCravingScore: Int?,
    var alcoholConsumed: Boolean?,
    var alcoholTypeAndAmount: String?,
    var sleepQualityScore: Int?,
    var appetiteScore: Int?,
    var emotionScore: Int?,
) {
    data class Builder(
        var alcoholCravingScore: Int? = null,
        var alcoholConsumed: Boolean? = false,
        var alcoholTypeAndAmount: String? = null,
        var sleepQualityScore: Int? = null,
        var appetiteScore: Int? = null,
        var emotionScore: Int? = null,
    ) {
        fun alcoholCravingScore(alcoholCravingScore: Int?) = apply {
            alcoholCravingScore?.let { if (it < 0 || it > 9) throw IllegalArgumentException("alcoholCravingScore must be between 0 and 9") }
            this.alcoholCravingScore = alcoholCravingScore
        }
        fun alcoholConsumed(alcoholConsumed: Boolean?) = apply { this.alcoholConsumed = alcoholConsumed }
        fun alcoholTypeAndAmount(alcoholTypeAndAmount: String?) = apply { this.alcoholTypeAndAmount = alcoholTypeAndAmount }
        fun sleepQualityScore(sleepQualityScore: Int?) = apply {
            sleepQualityScore?.let { if (it < 0 || it > 6) throw IllegalArgumentException("sleepQualityScore must be between 0 and 6") }
            this.sleepQualityScore = sleepQualityScore
        }
        fun appetiteScore(appetiteScore: Int?) = apply {
            appetiteScore?.let { if (it < 0 || it > 6) throw IllegalArgumentException("appetiteScore must be between 0 and 6") }
            this.appetiteScore = appetiteScore
        }
        fun emotionScore(emotionScore: Int?) = apply {
            emotionScore?.let { if (it < 0 || it > 6) throw IllegalArgumentException("emotionScore must be between 0 and 6") }
            this.emotionScore = emotionScore
        }
        fun build() = DiaryData(
            alcoholCravingScore,
            alcoholConsumed,
            alcoholTypeAndAmount,
            sleepQualityScore,
            appetiteScore,
            emotionScore,
        )
    }
}