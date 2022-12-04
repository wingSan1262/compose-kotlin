package vanrrtech.app.ajaib_app_sample.data.remote_repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.SearchPeopleResponse

interface SwapiRetrofitInterface {
    @GET("people")
    suspend fun getPeopleList(
        @Query("page") page : Int): SearchPeopleResponse?

    @GET("people")
    suspend fun searchPeople(
        @Query("search") searchKey : String,
        @Query("page") page : Int): SearchPeopleResponse?

}