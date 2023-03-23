package com.weltcorp.dta.waud.lib.diary.domain.model

data class DiaryMeta private constructor(
    val dateUnix: Int, //UnixTime in seconds (ex. 1675177200)
    val dateString: String?,
) {
    data class Builder(
        var dateUnix: Int = 0,
        var dateString: String? = null,
    ) {
        fun dateUnix(dateUnix: Int) = apply { this.dateUnix = dateUnix}
        fun dateString(dateString: String?) = apply { this.dateString = dateString}

        fun build() = DiaryMeta(dateUnix, dateString)
    }
}