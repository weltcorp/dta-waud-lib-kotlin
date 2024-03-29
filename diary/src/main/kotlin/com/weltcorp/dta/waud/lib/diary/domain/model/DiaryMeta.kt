package com.weltcorp.dta.waud.lib.diary.domain.model

data class DiaryMeta constructor(
    val answerId: Int, //UnixTime in seconds (ex. 1675177200)
    val dateUnix: Int, //UnixTime in seconds (ex. 1675177200)
    val dateString: String?,
    val description: String,
    val isCompleted: Boolean,
) {
    data class Builder(
        var answerId: Int = -1,
        var dateUnix: Int = 0,
        var dateString: String? = null,
        var description: String = "기록을 작성하세요",
        var isCompleted: Boolean = false,
    ) {
        fun answerId(answerId: Int) = apply { this.answerId = answerId}
        fun dateUnix(dateUnix: Int) = apply { this.dateUnix = dateUnix}
        fun dateString(dateString: String?) = apply { this.dateString = dateString}
        fun description(description: String) = apply { this.description = description}
        fun isCompleted(isCompleted: Boolean) = apply { this.isCompleted = isCompleted}

        fun build() = DiaryMeta(answerId, dateUnix, dateString, description, isCompleted)
    }
}