package com.example.testapplication.data.remote_repository

import retrofit2.Retrofit

/**
 * retrofit client holder into one class
 * this way, we will only need one instance of retrofit
 * but reused for many interface in Appscope atleast
 */
class RemoteApiRetrofitClient(
    val retrofit : Retrofit
) {

    /**
     * please add Base URL here if you need some new base url
     */
    companion object{
        val BASE_URL_SWAPI = "https://swapi.dev/api/"
    }

    /**
     * please add new interface if needed
     * as an object provider for DI
     */
    fun getSwapiInterface(): SwapiRetrofitInterface {
        return retrofit
            .newBuilder()
            .baseUrl(BASE_URL_SWAPI)
            .build()
            .create(SwapiRetrofitInterface::class.java)
    }

}