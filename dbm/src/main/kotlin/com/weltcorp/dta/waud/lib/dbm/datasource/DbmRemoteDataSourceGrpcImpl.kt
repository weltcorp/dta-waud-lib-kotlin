package com.weltcorp.dta.waud.lib.dbm.datasource

import com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode
import com.weltcorp.dta.waud.lib.dbm.DbmApiConfig
import com.weltcorp.dta.waud.lib.dbm.domain.model.KindInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.SourceInfo
import dtd.api.dbm.v1.dbm.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import java.time.ZonedDateTime

private const val DOMAIN_ID = "100"
private const val PROJECT_ID = "9" // WAUD
private const val OS = "Android"
private const val DBM_VERSION = "2.3"
private const val USER_STATUS = "1"

class DbmRemoteDataSourceGrpcImpl(private val config: DbmApiConfig) : RemoteDataSource {

    private var _channel = getMangedChannel()
    private var _stub = DbmDataGrpcKt.DbmDataCoroutineStub(getChannel())

    private fun getHeader(): Metadata {
        return Metadata().apply {
            put(Metadata.Key.of("x-request-dtx-src-account-type", Metadata.ASCII_STRING_MARSHALLER), "0")
            put(Metadata.Key.of("x-request-dtx-src-domain-id", Metadata.ASCII_STRING_MARSHALLER), DOMAIN_ID)
            put(Metadata.Key.of("x-request-dtx-src-service-name", Metadata.ASCII_STRING_MARSHALLER), "dta-waud-lib-kotlin")
            put(Metadata.Key.of("x-request-dtx-dst-protocol", Metadata.ASCII_STRING_MARSHALLER), "grpc")
            put(Metadata.Key.of("x-request-dtx-dst-service-name", Metadata.ASCII_STRING_MARSHALLER), "dtd-api-dbm")
            put(Metadata.Key.of("x-request-dtx-dst-service-version", Metadata.ASCII_STRING_MARSHALLER), "v1")
            put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + config.auth)
        }
    }

    private fun getChannel(): ManagedChannel {
        if (_channel.isShutdown || _channel.isTerminated) {
            _channel = getMangedChannel()
        }
        return _channel
    }

    private fun getMangedChannel(): ManagedChannel {
        val channel = ManagedChannelBuilder
            .forTarget("${config.host}${if (config.port != null) ":${config.port}" else ""}")
        if (config.port != null) {
            channel.usePlaintext()
        }
        return channel.build()
    }

    private fun stub(): DbmDataGrpcKt.DbmDataCoroutineStub {
        if (_stub == null) {
            _stub = DbmDataGrpcKt.DbmDataCoroutineStub(getChannel())
        }
        return _stub
    }

    @OptIn(OnlyForUseByGeneratedProtoCode::class)
    override suspend fun createDbmRecord(osEnv: String, projectId: Int, userId: Int, categoryValue: String, sourceInfo: SourceInfo, kindInfoList: List<KindInfo>) {

        val source = source {
            value = sourceInfo.value
            hardwareVersion = sourceInfo.hardwareVersion
            softwareVersion = sourceInfo.softwareVersion
            deviceName = sourceInfo.deviceName
            hardwareName = sourceInfo.hardwareName
            bundleIdentifier = sourceInfo.bundleIdentifier
            for (kindInfo in kindInfoList) {
                kinds.add(kindInfo.toKind(categoryValue))
            }
        }

        val category = category {
            value = categoryValue
            sources.add(source)
        }

        val request = dbm {
            this.os = OS
            this.osEnv = osEnv
            this.version = DBM_VERSION
            this.domainId = DOMAIN_ID
            this.projectId = projectId.toString()
            this.userId = userId.toString()
            this.status = USER_STATUS
            this.timestamp = ZonedDateTime.now().toEpochSecond().toString()
            this.category = category
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
        }

        stub().recordDbm(request, header)
    }
}

private fun KindInfo.toKind(categoryValue: String, ): DbmV1Dbm.Kind {
    val _this = this
    return kind {
        if (categoryValue === "stepCount") {
            this.stepCount = _this.value
        } else if (categoryValue === "distance") {
            this.distance = _this.value
        } else if (categoryValue === "heartRate") {
            this.heartRate = _this.value
        } else if (categoryValue === "sleepState") {
            this.sleepState = _this.value
        } else if (categoryValue === "walkingSpeed") {
            this.walkingSpeed = _this.value
        } else if (categoryValue === "walkingStepLength") {
            this.walkingStepLength = _this.value
        }else if (categoryValue === "latitude") {
            this.latitude = _this.value
        } else if (categoryValue === "longitude") {
            this.longitude = _this.value
        } else if (categoryValue === "floor") {
            this.floor = _this.value
        } else if (categoryValue === "accuracy") {
            this.accuracy = _this.value
        } else if (categoryValue === "horizontalAccuracy") {
            this.horizontalAccuracy = _this.value
        } else if (categoryValue === "altitude") {
            this.altitude = _this.value
        } else if (categoryValue === "verticalAccuracy") {
            this.verticalAccuracy = _this.value
        } else if (categoryValue === "speed") {
            this.speed = _this.value
        } else if (categoryValue === "speedAccuracy") {
            this.speedAccuracy = _this.value
        } else if (categoryValue === "course") {
            this.course = _this.value
        } else if (categoryValue === "courseAccuracy") {
            this.courseAccuracy = _this.value
        }else if (categoryValue === "bearing") {
            this.bearing = _this.value
        } else if (categoryValue === "duration") {
            this.duration = _this.value
        } else if (categoryValue === "steps") {
            this.steps = _this.value
        }

        if (_this.time != null) {
            this.time = _this.time
        }
        if (_this.startTime != null) {
            this.startTime = _this.startTime
        }
        if (_this.endTime != null) {
            this.endTime = _this.endTime
        }
        if (_this.date != null) {
            this.date = _this.date
        }
        if (_this.startDate != null) {
            this.startDate = _this.startDate
        }
        if (_this.endDate != null) {
            this.endDate = _this.endDate
        }
    }
}