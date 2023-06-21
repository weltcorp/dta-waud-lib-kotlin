package com.weltcorp.dta.waud.lib.dbm.datasource

import com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode
import com.weltcorp.dta.waud.lib.dbm.DbmApiConfig
import com.weltcorp.dta.waud.lib.dbm.domain.model.DbmRecordInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.KindInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.SourceInfo
import dtd.api.dbm.v3.dbm.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import java.time.ZonedDateTime

private const val DOMAIN_ID = "100"
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
            put(Metadata.Key.of("x-request-dtx-dst-service-version", Metadata.ASCII_STRING_MARSHALLER), "v3")
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
    override suspend fun createDbmRecord(
        osEnv: String,
        projectId: Int,
        userId: Int,
        category: String,
        sourceInfo: SourceInfo,
        kindInfoList: List<KindInfo>
    ) {

        val dbmSource = source {
            value = sourceInfo.value
            hardwareVersion = sourceInfo.hardwareVersion
            softwareVersion = sourceInfo.softwareVersion
            deviceName = sourceInfo.deviceName
            hardwareName = sourceInfo.hardwareName
            bundleIdentifier = sourceInfo.bundleIdentifier
            for (kindInfo in kindInfoList) {
                kinds.add(kindInfo.toKind(category))
            }
        }

        val dbmCategory = category {
            value = category
            sources.add(dbmSource)
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
            this.category = dbmCategory
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
        }

        stub().recordDbm(request, header)
    }

    override suspend fun createDbmRecordList(
        osEnv: String,
        projectId: Int,
        userId: Int,
        dbmRecordInfoList: List<DbmRecordInfo>,
        sourceInfo: SourceInfo,
    ) {

        val request = recordDbmsRequest {
            dbmRecordInfoList.forEach { dbmRecordInfo ->
                dbmRecordInfo.kindInfoList.forEach { kindInfo ->
                    val source = source {
                        value = sourceInfo.value
                        hardwareVersion = sourceInfo.hardwareVersion
                        softwareVersion = sourceInfo.softwareVersion
                        deviceName = sourceInfo.deviceName
                        hardwareName = sourceInfo.hardwareName
                        bundleIdentifier = sourceInfo.bundleIdentifier
                        kinds.add(kindInfo.toKind(dbmRecordInfo.category))
                    }

                    val category = category {
                        value = dbmRecordInfo.category
                        sources.add(source)
                    }

                    this.dbms.add(
                        dbm {
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
                    )
                }
            }
        }

        val header = getHeader().apply {
            put(Metadata.Key.of("x-request-dtx-user-id", Metadata.ASCII_STRING_MARSHALLER), "$userId")
        }

        stub().recordDbms(request, header)
    }
}

private fun KindInfo.toKind(category: String, ): DbmV3Dbm.Kind {
    val _this = this
    return kind {
        if (category === "value") {
            this.value = _this.data
        } else if (category === "stepCount") {
            this.stepCount = _this.data
        } else if (category === "distance") {
            this.distance = _this.data
        } else if (category === "heartRate") {
            this.heartRate = _this.data
        } else if (category === "sleepState") {
            this.sleepState = _this.data
        } else if (category === "walkingSpeed") {
            this.walkingSpeed = _this.data
        } else if (category === "walkingStepLength") {
            this.walkingStepLength = _this.data
        } else if (category === "latitude") {
            this.latitude = _this.data
        } else if (category === "longitude") {
            this.longitude = _this.data
        } else if (category === "floor") {
            this.floor = _this.data
        } else if (category === "accuracy") {
            this.accuracy = _this.data
        } else if (category === "horizontalAccuracy") {
            this.horizontalAccuracy = _this.data
        } else if (category === "altitude") {
            this.altitude = _this.data
        } else if (category === "verticalAccuracy") {
            this.verticalAccuracy = _this.data
        } else if (category === "speed") {
            this.speed = _this.data
        } else if (category === "speedAccuracy") {
            this.speedAccuracy = _this.data
        } else if (category === "course") {
            this.course = _this.data
        } else if (category === "courseAccuracy") {
            this.courseAccuracy = _this.data
        } else if (category === "bearing") {
            this.bearing = _this.data
        } else if (category === "steps") {
            this.steps = _this.data
        } else if (category === "healthConnectDistance") {
            this.healthConnectDistance = _this.data
        } else if (category === "healthConnectExerciseSession") {
            this.healthConnectExerciseSession = _this.data
        } else if (category === "healthConnectHeartRate") {
            this.healthConnectHeartRate = _this.data
        } else if (category === "healthConnectSleepStage") {
            this.healthConnectSleepStage = _this.data
        } else if (category === "healthConnectSpeed") {
            this.healthConnectSpeed = _this.data
        } else if (category === "healthConnectSteps") {
            this.healthConnectSteps = _this.data
        } else if (category === "healthConnectStepsCadence") {
            this.healthConnectStepsCadence = _this.data
        } else if (category === "healthConnectTotalCaloriesBurned") {
            this.healthConnectTotalCaloriesBurned = _this.data
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
        if (_this.duration != null) {
            this.duration = _this.duration
        }

    }
}