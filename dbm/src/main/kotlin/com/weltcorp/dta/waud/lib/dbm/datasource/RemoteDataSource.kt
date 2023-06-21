package com.weltcorp.dta.waud.lib.dbm.datasource

import com.weltcorp.dta.waud.lib.dbm.domain.model.DbmRecordInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.KindInfo
import com.weltcorp.dta.waud.lib.dbm.domain.model.SourceInfo

interface RemoteDataSource {

    suspend fun createDbmRecord(
        osEnv: String,
        projectId: Int,
        userId: Int,
        category: String,
        sourceInfo: SourceInfo,
        kindInfoList: List<KindInfo>
    )

    suspend fun createDbmRecordList(
        osEnv: String,
        projectId: Int,
        userId: Int,
        dbmRecordInfoList: List<DbmRecordInfo>,
        sourceInfo: SourceInfo,
    )
}