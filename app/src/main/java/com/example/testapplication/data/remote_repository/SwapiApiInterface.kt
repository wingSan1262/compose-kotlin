package com.example.testapplication.data.remote_repository

import com.example.testapplication.base_component.entities.ResourceState
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.QueryPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemResponse
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.SearchPeopleResponse

interface SwapiApiInterface {

    suspend fun getPeopleList(
        queryPeopleRequest: QueryPeopleRequest
    ):SearchPeopleResponse?
    suspend fun searchPeopleList(
        searchPeopleRequest: SearchPeopleRequest
    ):SearchPeopleResponse?


    suspend fun getOfflinePeopleList():List<PeopleItemResponse>?
    suspend fun setOfflineCachePeopleList(list :List<PeopleItemResponse>? ): Boolean
}