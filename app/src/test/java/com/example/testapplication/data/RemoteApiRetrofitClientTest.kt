package com.example.testapplication.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testapplication.data.remote_repository.SwapiApiImpl
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.After
import org.junit.Rule
import com.example.testapplication.data.local_db.PeopleItemDao
import com.example.testapplication.data.remote_repository.SwapiRetrofitInterface
import com.example.testapplication.domain.models.request.QueryPeopleRequest
import com.example.testapplication.domain.models.request.SearchPeopleRequest
import com.example.testapplication.domain.models.response.PeopleItemResponse
import com.example.testapplication.domain.models.response.SearchPeopleResponse

@RunWith(MockitoJUnitRunner::class)
internal class RemoteApiRetrofitClientTest{
    lateinit var SUT: SwapiApiInterface
    @MockK lateinit var retrofit: SwapiRetrofitInterface
    @MockK lateinit var dao: PeopleItemDao
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()
    val dispatcher = TestCoroutineDispatcher()

    val requestSimpleQuery = QueryPeopleRequest(1)
    val searchPeopleRequest = SearchPeopleRequest(
        "hi", 1
    )
    val simplePeopleResponse = SearchPeopleResponse(
        1, listOf(
            PeopleItemResponse()
        )
    )
    val simpleListResponse = listOf(
        PeopleItemResponse()
    )

    @Before @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        SUT = SwapiApiImpl(retrofit, dao)
    }
    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test fun getPeopleList_success() {
        coEvery{
            retrofit.getPeopleList(requestSimpleQuery.page)
        } returns (simplePeopleResponse)
        runBlocking {
            if(SUT.getPeopleList(requestSimpleQuery) == null)
                throw Throwable("fail, null")
        }
    }
    @Test fun getPeopleList_Fail() {
        coEvery{
            retrofit.getPeopleList(requestSimpleQuery.page)
        } returns (null)
        runTest {
            if(SUT.getPeopleList(requestSimpleQuery) != null)
                throw Throwable("fail, null")
        }
    }
    @Test fun getPeopleList_ArgumentCapting() {
        var slot = slot<Int>()
        coEvery{
            retrofit.getPeopleList(capture(slot))
        } returns (simplePeopleResponse)
        runTest {
            if(SUT.getPeopleList(requestSimpleQuery) == null)
                throw Throwable("fail, null")

            assertThat(requestSimpleQuery.page, `is`(slot.captured))
        }
    }

    @Test fun searchPeopleList_success() {
        coEvery{
            retrofit.searchPeople(
                searchPeopleRequest.query,
                searchPeopleRequest.page
            )
        } returns (simplePeopleResponse)
        runTest {
            if(SUT.searchPeopleList(searchPeopleRequest) == null)
                throw Throwable("fail, null")
        }
    }
    @Test fun searchPeopleList_Fail() {
        coEvery{
            retrofit.searchPeople(
                searchPeopleRequest.query,
                searchPeopleRequest.page
            )
        } returns (null)
        runTest {
            if(SUT.searchPeopleList(searchPeopleRequest) != null)
                throw Throwable("fail, null")
        }
    }
    @Test fun searchPeopleList_ArgumentCapting() {
        var slot1 = slot<String>()
        var slot2 = slot<Int>()
        coEvery{
            retrofit.searchPeople(
                capture(slot1),
                capture(slot2)
            )
        } returns (simplePeopleResponse)
        runTest {
            if(SUT.searchPeopleList(searchPeopleRequest) == null)
                throw Throwable("fail, null")
            assertThat(searchPeopleRequest.query, `is`(slot1.captured))
            assertThat(searchPeopleRequest.page, `is`(slot2.captured))
        }
    }

    @Test fun getOfflinePeopleList_success() {
        coEvery{
            dao.loadAllUser()
        } returns (simpleListResponse)
        runTest {
            if(SUT.getOfflinePeopleList() == null)
                throw Throwable("fail, null")
            val data = SUT.getOfflinePeopleList()
            assertThat(data, `is`(simpleListResponse))
        }
    }
    @Test fun getOfflinePeopleList_fail() {
        coEvery{
            dao.loadAllUser()
        } returns (null)
        runTest {
            if(SUT.getOfflinePeopleList() != null)
                throw Throwable("fail, null")
        }
    }

    @Test fun setOfflineCachePeopleList_errorNuke() {
        coEvery{
            dao.nukeTable()
        }.throws(Throwable("some error"))
        runTest {
            if(SUT.setOfflineCachePeopleList(
                    simpleListResponse))
                throw Throwable("fail, null")
        }
    }

    @Test fun setOfflineCachePeopleList_errorInsert() {
        coEvery{
            dao.nukeTable()
        }.returns(Unit)
        coEvery{
            dao.insertUsers(any())
        }.throws(Throwable("some error"))
        runTest {
            if(SUT.setOfflineCachePeopleList(
                    simpleListResponse))
                throw Throwable("fail, null")
        }
    }
    @Test fun setOfflineCachePeopleList_success() {
        var slot1 = slot<PeopleItemResponse>()
        coEvery{
            dao.nukeTable()
        }.returns(Unit)
        coEvery{
            dao.insertUsers(capture(slot1))
        }.returns(Unit)
        runTest {
            if(!SUT.setOfflineCachePeopleList(
                    simpleListResponse))
                throw Throwable("fail, null")
            assertThat(
                simpleListResponse.get(0), `is`(slot1.captured)
            )
        }
    }


}