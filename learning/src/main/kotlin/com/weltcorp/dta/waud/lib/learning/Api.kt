package com.weltcorp.dta.waud.lib.learning

class LearningApiConfig private constructor (
    var host: String? = null,
    var port: Int? = null,
    var auth: String,
) {

    data class Builder(
        var host: String = "localhost",
        var port: Int? = null,
        var auth: String = "",
    ) {
        fun host(host: String) = apply { this.host = host }
        fun port(port: Int?) = apply { this.port = port }
        fun auth(auth: String) = apply { this.auth = auth }
        fun build() = LearningApiConfig(host, port, auth)
    }
}