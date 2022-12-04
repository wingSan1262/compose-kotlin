package com.example.testapplication.feature

import junit.framework.TestCase


import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testapplication.base_component.entities.ResourceState
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import com.example.testapplication.domain.usecases.FetchOfflinePeopleListUseCase
import com.example.testapplication.domain.usecases.FetchPeopleUseCase
import com.example.testapplication.domain.usecases.SearchPeopleUseCase
import com.example.testapplication.domain.usecases.UpdateOfflinePeopleListUseCase
import com.example.testapplication.feature.SearchViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Rule
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.*

@RunWith(MockitoJUnitRunner::class)

class SearchViewModelTest{
lateinit var SUT: SearchViewModel

var fetchPeopleUseCase: FetchPeopleUseCase? = null
var searchPeopleUseCase: SearchPeopleUseCase? = null
var fetchOfflinePeopleListUseCase: FetchOfflinePeopleListUseCase? = null
var updateOfflinePeopleListUseCase: UpdateOfflinePeopleListUseCase? = null
@MockK
lateinit var mApi: SwapiApiInterface

@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

val dispatcher = TestCoroutineDispatcher()

@Before
@Throws(Exception::class)
fun setup() {
    MockKAnnotations.init(this)
    Dispatchers.setMain(dispatcher)
    fetchPeopleUseCase = FetchPeopleUseCase(mApi)
    searchPeopleUseCase = SearchPeopleUseCase(mApi)
    fetchOfflinePeopleListUseCase = FetchOfflinePeopleListUseCase(mApi)
    updateOfflinePeopleListUseCase = UpdateOfflinePeopleListUseCase(mApi)
    SUT = SearchViewModel(
        fetchPeopleUseCase!!,
        searchPeopleUseCase!!,
        fetchOfflinePeopleListUseCase!!,
        updateOfflinePeopleListUseCase!!
    )
}

@Test
fun callApi_fetchUserList_invokedCorrectlyAndSuccess(){
    val slot = slot<SearchPeopleRequest>()
    val req = SearchPeopleRequest("hi", 1)
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.searchPeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery { mApi.searchPeopleList(capture(slot))}.returns(res)
        SUT?.searchPeopleData(req)
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Success)){
            assertThat(
                (result as ResourceState.Success<PeopleListModel>).body.results?.get(0)?.name
                , `is`(res.results?.get(0)?.name))
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_fetchUserList_currentPageUpdateCache(){
    SUT.currentPage = 1
    val slot = slot<SearchPeopleRequest>()
    val slot2 = slot<List<PeopleItemResponse>>()
    val req = SearchPeopleRequest("hi", SUT.currentPage)
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.searchPeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }

    runTest {
        coEvery { mApi.searchPeopleList(capture(slot))}.returns(res)
        coEvery { mApi.setOfflineCachePeopleList(capture(slot2))}.returns(true)
        SUT?.searchPeopleData(req)
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Success)){
            assertThat(
                (result as ResourceState.Success<PeopleListModel>).body.results?.get(0)?.name
                , `is`(res.results?.get(0)?.name))
            coVerify(exactly = 1){mApi.setOfflineCachePeopleList(any())}
            assertThat(res.results, `is`(slot2.captured))
            assertThat(res.results?.get(0)?.name, `is`(slot2.captured.get((0)).name))
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_fetchUserList_notStartingPageNotUpdateCache(){
    SUT.currentPage = 2
    val slot = slot<SearchPeopleRequest>()
    val req = SearchPeopleRequest("hi", SUT.currentPage)
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.searchPeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }

    runTest {
        coEvery { mApi.searchPeopleList(capture(slot))}.returns(res)
        SUT?.searchPeopleData(req)
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Success)){
            assertThat(
                (result as ResourceState.Success<PeopleListModel>).body.results?.get(0)?.name
                , `is`(res.results?.get(0)?.name))
            coVerify(exactly = 0){mApi.setOfflineCachePeopleList(any())}
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_fetchUserList_errorThrown(){
    val slot = slot<SearchPeopleRequest>()
    val req = SearchPeopleRequest("hi", 1)
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.searchPeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery{mApi.searchPeopleList(capture(slot))}.throws(Throwable("something error"))
        SUT?.searchPeopleData(req)
        for(i in 0..5){
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Failure)){
            if((result as ResourceState.Failure<PeopleListModel>).exception.message.isNullOrEmpty())
                throw Throwable("error empty")
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_fetchUserList_apiReturnNull(){
    val slot = slot<SearchPeopleRequest>()
    val req = SearchPeopleRequest("hi", 1)
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.searchPeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery{mApi.searchPeopleList(capture(slot))}.returns(null)
        SUT?.searchPeopleData(req)
        for(i in 0..5){
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Failure)){
            if((result as ResourceState.Failure<PeopleListModel>).exception.message.isNullOrEmpty())
                throw Throwable("error empty")
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}

@Test
fun callApi_updateOfflineCachingPeople_invokedCorrectlyAndSuccess(){
    val slot = slot<List<PeopleItemResponse>>()
    val req = listOf(PeopleItemResponse(name = "hi"))
    var isNotified = false
    var result  : ResourceState<Boolean>? = null
    SUT.updateOfflinePeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery { mApi.setOfflineCachePeopleList(capture(slot))}.returns(true)
        SUT?.updateOfflineCachingPeople(req)
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.setOfflineCachePeopleList(any())}
        coVerify(exactly = 0){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Success)){
            assertThat(
                ((result as ResourceState.Success<Boolean>).body)
                , `is`(true))
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_updateOfflineCachingPeople_errorThrown(){
    val slot = slot<List<PeopleItemResponse>>()
    val req = listOf(PeopleItemResponse(name = "hi"))
    var isNotified = false
    var result  : ResourceState<Boolean>? = null
    SUT.updateOfflinePeopleLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery { mApi.setOfflineCachePeopleList(capture(slot))}.throws(Throwable("some error"))
        SUT?.updateOfflineCachingPeople(req)
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.setOfflineCachePeopleList(any())}
        coVerify(exactly = 0){mApi.searchPeopleList(any())}
        assertThat(req, `is`(slot.captured))
        if((result is ResourceState.Success)){
            assertThat(
                ((result as ResourceState.Success<Boolean>).body)
                , `is`(false))
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}


@Test
fun callApi_fetchOfflineCachePeopleList_invokedCorrectlyAndSuccess(){
    val slot = slot<SearchPeopleRequest>()
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.offlineCachingLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }

    runTest {

        coEvery { mApi.getOfflinePeopleList()}.returns(listOf(PeopleItemResponse(name = "hi")))
        SUT?.fetchOfflineCachePeopleList()
        while(!isNotified){
            delay(500)
        }
        coVerify(exactly = 1){mApi.getOfflinePeopleList()}
        if((result is ResourceState.Success)){
            assertThat(
                (result as ResourceState.Success<PeopleListModel>).body.results?.get(0)?.name
                , `is`(res.results?.get(0)?.name))
            assertThat(res.count, `is`((result as ResourceState.Success<PeopleListModel>).body.results!!.size))
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}
@Test
fun callApi_fetchOfflineCachePeopleList_errorThrown(){
    val slot = slot<SearchPeopleRequest>()
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.offlineCachingLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery { mApi.getOfflinePeopleList()}.throws(Throwable("an error"))
        SUT?.fetchOfflineCachePeopleList()
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.getOfflinePeopleList()}
        coVerify(exactly = 0){mApi.getPeopleList(any())}
        coVerify(exactly = 0){mApi.setOfflineCachePeopleList(any())}
        if((result is ResourceState.Failure)){
            if((result as ResourceState.Failure<PeopleListModel>).exception.message.isNullOrEmpty())
                throw Throwable("error empty")
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}

@Test
fun callApi_fetchOfflineCachePeopleList_nullResult(){
    val slot = slot<SearchPeopleRequest>()
    val res = SearchPeopleResponse(
        1, listOf(PeopleItemResponse(name = "hi"))
    )
    var isNotified = false
    var result  : ResourceState<PeopleListModel>? = null
    SUT.offlineCachingLiveData.observeForever{
        it.contentIfNotHandled?.let {
            isNotified = true; result = it
        }
    }
    runTest {
        coEvery { mApi.getOfflinePeopleList()}.returns(null)
        SUT?.fetchOfflineCachePeopleList()
        for(i in 0..5) {
            delay(250)
            if(isNotified)
                break
        }
        coVerify(exactly = 1){mApi.getOfflinePeopleList()}
        coVerify(exactly = 0){mApi.getPeopleList(any())}
        coVerify(exactly = 0){mApi.setOfflineCachePeopleList(any())}
        if((result is ResourceState.Failure)){
            if((result as ResourceState.Failure<PeopleListModel>).exception.message.isNullOrEmpty())
                throw Throwable("error empty")
        } else {
            throw Throwable("call fail")
        }
        advanceUntilIdle()
    }
}

@Test
fun onDestroy_jobAllCancel(){
    SUT.onDestroy()
    assertThat(fetchPeopleUseCase!!.isCancelled(), `is`(true))
    assertThat(searchPeopleUseCase!!.isCancelled(), `is`(true))
    assertThat(fetchOfflinePeopleListUseCase!!.isCancelled(), `is`(true))
    assertThat(updateOfflinePeopleListUseCase!!.isCancelled(), `is`(true))
}

@Test
fun jobActiveAtIdle(){
    assertThat(fetchPeopleUseCase!!.isCancelled(), `is`(false))
    assertThat(searchPeopleUseCase!!.isCancelled(), `is`(false))
    assertThat(fetchOfflinePeopleListUseCase!!.isCancelled(), `is`(false))
    assertThat(updateOfflinePeopleListUseCase!!.isCancelled(), `is`(false))
}

@Test
fun checkParamAtStart(){
    assertThat(SUT.isFirstOpen, `is`(true))
    assertThat(SUT.isQuerying, `is`(false))
    assertThat(SUT.maxCount, `is`(0))
    assertThat(SUT.currentPage, `is`(1))
    assertThat(SUT.currentQuerySearch, `is`(""))
}

@Test
fun checkParamAfterChange(){
    SUT.run {
        isFirstOpen = false // for the sake theatrical list opening hehe
        isQuerying = true
        maxCount = 82
        currentPage = 7
        currentQuerySearch = "some"
    }


    assertThat(SUT.isFirstOpen, `is`(false))
    assertThat(SUT.isQuerying, `is`(true))
    assertThat(SUT.maxCount, `is`(82))
    assertThat(SUT.currentPage, `is`(7))
    assertThat(SUT.currentQuerySearch, `is`("some"))
}





}