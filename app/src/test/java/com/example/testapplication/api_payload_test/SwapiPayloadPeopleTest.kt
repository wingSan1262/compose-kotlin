package com.example.testapplication.api_payload_test


import android.R
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.viewbinding.ViewBinding
import com.example.testapplication.data.remote_repository.SwapiApiImpl
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.mockito.Mock
import vanrrtech.app.ajaib_app_sample.data.SQDb.github.LocalDb
import vanrrtech.app.ajaib_app_sample.data.SQDb.github.PeopleItemDao
import vanrrtech.app.ajaib_app_sample.data.remote_repository.SwapiRetrofitInterface
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.QueryPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest


@RunWith(MockitoJUnitRunner::class)
class SwapiPayloadPeopleTest {

    lateinit var SUT: SwapiApiInterface
    private val mockWebServer = MockWebServer()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var dao : PeopleItemDao

    val dispatcher = TestCoroutineDispatcher()

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)

        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SwapiRetrofitInterface::class.java).apply{
                SUT = SwapiApiImpl(
                    this,
                    dao
                )
            }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testQueryPeopleList_success(){
        mockWebServer.enqueue(
            MockResponse().also {
                it.setBody(SWAPI_PEOPLE_PAYLOAD)
                it.setResponseCode(200)
            }
        )
        runTest {
            val payloadObject = SUT.getPeopleList(
                QueryPeopleRequest(1)
            )
            payloadObject?.run {
                assertThat(count, `is`(1))
                results?.run{
                    assertThat(results?.get(0)!!.name, `is`("AnakinSkywalker"))
                    assertThat(results?.get(0)!!.height, `is`("188"))
                    assertThat(results?.get(0)!!.gender, `is`("male"))
                } ?: run {
                    throw Throwable("data not parsed correctly")
                }
            }?: run {
                throw Throwable("data not parsed correctly")
            }
            advanceUntilIdle()
        }
    }

    @Test
    fun testSearchPeopleList_success(){
        mockWebServer.enqueue(
            MockResponse().also {
                it.setBody(SWAPI_PEOPLE_PAYLOAD)
                it.setResponseCode(200)
            }
        )
        runTest {
            val payloadObject = SUT.searchPeopleList(
                SearchPeopleRequest("skywalker",1) // note : not captoring here
            )
            payloadObject?.run {
                assertThat(count, `is`(1))
                results?.run{
                    assertThat(results?.get(0)!!.name, `is`("AnakinSkywalker"))
                    assertThat(results?.get(0)!!.height, `is`("188"))
                    assertThat(results?.get(0)!!.gender, `is`("male"))
                } ?: run {
                    throw Throwable("data not parsed correctly")
                }
            }?: run {
                throw Throwable("data not parsed correctly")
            }
            advanceUntilIdle()
        }
    }


    companion object{
        val SWAPI_PEOPLE_PAYLOAD = "{\"count\":1,\"next\":null,\"previous\":null,\"results\":[{\"name\":\"AnakinSkywalker\",\"height\":\"188\",\"mass\":\"84\",\"hair_color\":\"blond\",\"skin_color\":\"fair\",\"eye_color\":\"blue\",\"birth_year\":\"41.9BBY\",\"gender\":\"male\",\"homeworld\":\"https://swapi.dev/api/planets/1/\",\"films\":[\"https://swapi.dev/api/films/4/\",\"https://swapi.dev/api/films/5/\",\"https://swapi.dev/api/films/6/\"],\"species\":[],\"vehicles\":[\"https://swapi.dev/api/vehicles/44/\",\"https://swapi.dev/api/vehicles/46/\"],\"starships\":[\"https://swapi.dev/api/starships/39/\",\"https://swapi.dev/api/starships/59/\",\"https://swapi.dev/api/starships/65/\"],\"created\":\"2014-12-10T16:20:44.310000Z\",\"edited\":\"2014-12-20T21:17:50.327000Z\",\"url\":\"https://swapi.dev/api/people/11/\"}]}"
    }
}