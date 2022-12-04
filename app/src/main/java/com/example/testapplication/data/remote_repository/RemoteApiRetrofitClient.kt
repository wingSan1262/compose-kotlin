package vanrrtech.app.ajaib_app_sample.data.remote_repository

import retrofit2.Retrofit

class RemoteApiRetrofitClient(
    val retrofit : Retrofit
) {

    companion object{
        val BASE_URL_SWAPI = "https://swapi.dev/api/"
    }

    fun getSwapiInterface(): SwapiRetrofitInterface {
        return retrofit
            .newBuilder()
            .baseUrl(BASE_URL_SWAPI)
            .build()
            .create(SwapiRetrofitInterface::class.java)
    }

}