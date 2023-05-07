package com.weltcorp.dtx.core.lib.project.datasource

import com.weltcorp.dtx.core.lib.project.ProjectApiConfig
import com.weltcorp.dtx.core.lib.project.domain.model.ProjectUserStatus

class ProjectRemoteDataSourceHttpImpl(private val config: ProjectApiConfig) : ProjectRemoteDataSource {
    override suspend fun getProjectUserStatus(userId: Int): ProjectUserStatus {
        TODO("Not yet implemented")
    }
}