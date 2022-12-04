package com.example.testapplication.data.remote_repository

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.testapplication.domain.models.response.SearchPeopleResponse

interface SwapiRetrofitInterface {
    @GET("people")
    suspend fun getPeopleList(
        @Query("page") page : Int): SearchPeopleResponse?

    @GET("people")
    suspend fun searchPeople(
        @Query("search") searchKey : String,
        @Query("page") page : Int): SearchPeopleResponse?

}