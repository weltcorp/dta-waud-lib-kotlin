package com.weltcorp.dtx.core.lib.project.datasource

import com.weltcorp.dtx.core.lib.project.domain.model.ProjectUserStatus

interface ProjectRemoteDataSource {

    suspend fun getProjectUserStatus(userId: Int): ProjectUserStatus
}