package com.example.testapplication.data.remote_repository

import com.example.testapplication.domain.models.request.QueryPeopleRequest
import com.example.testapplication.domain.models.request.SearchPeopleRequest
import com.example.testapplication.domain.models.response.PeopleItemResponse
import com.example.testapplication.domain.models.response.SearchPeopleResponse

/**
 * this is interface class for give some abstraction level
 * on SwapiApi implementation
 */

interface SwapiApiInterface {

    suspend fun getPeopleList(
        queryPeopleRequest: QueryPeopleRequest
    ): SearchPeopleResponse?
    suspend fun searchPeopleList(
        searchPeopleRequest: SearchPeopleRequest
    ): SearchPeopleResponse?


    suspend fun getOfflinePeopleList():List<PeopleItemResponse>?
    suspend fun setOfflineCachePeopleList(list :List<PeopleItemResponse>? ): Boolean
}