package com.example.testapplication.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.testapplication.base_component.entities.Event
import com.example.testapplication.base_component.entities.ResourceState
import com.example.testapplication.domain.usecases.FetchOfflinePeopleListUseCase
import com.example.testapplication.domain.usecases.FetchPeopleUseCase
import com.example.testapplication.domain.usecases.SearchPeopleUseCase
import com.example.testapplication.domain.usecases.UpdateOfflinePeopleListUseCase
import com.example.testapplication.domain.models.request.SearchPeopleRequest
import com.example.testapplication.feature.model.PeopleItemModel
import com.example.testapplication.domain.models.response.PeopleItemResponse
import com.example.testapplication.feature.model.PeopleListModel

/**
 * view model for Searching Functions
 *
 * dont forget to call onDestroy() when the dependant is destroyed
 * TODO also . . . maybe it's a good thing to pass the lifeCycleScope to usecase as Coroutine Scope
 *
 */
class SearchViewModel(
    val fetchPeopleUseCase: FetchPeopleUseCase,
    val searchPeopleUseCase: SearchPeopleUseCase,
    val fetchOfflinePeopleListUseCase: FetchOfflinePeopleListUseCase,
    val updateOfflinePeopleListUseCase: UpdateOfflinePeopleListUseCase
) : ViewModel(){

    var isFirstOpen = true // for the sake theatrical list opening hehe
    var isQuerying = false
    var maxCount = 0
    var currentPage = 1
    var currentQuerySearch = ""

    val searchPeopleLiveData = Transformations.switchMap(searchPeopleUseCase.currentData){
        val livedata = MutableLiveData<Event<ResourceState<PeopleListModel>>>()
        it.contentIfNotHandled?.let {
            if(it is ResourceState.Success){
                livedata.value = Event(
                    ResourceState.Success(
                        PeopleListModel(it.body)
                    )
                )
                // todo safe caching here
                if(currentPage == 1)
                    it.body.results?.let { it1 -> updateOfflineCachingPeople(it1) } }
            if(it is ResourceState.Failure){
                livedata.value = Event(ResourceState.Failure(it.exception))}
        }
        livedata
    }
    fun searchPeopleData (param: SearchPeopleRequest){
        isQuerying = true
        searchPeopleUseCase.setup(param)
    }

    val offlineCachingLiveData = Transformations.switchMap(fetchOfflinePeopleListUseCase.currentData){
            val livedata = MutableLiveData<Event<ResourceState<PeopleListModel>>>()
            it.contentIfNotHandled?.let {
                if(it is ResourceState.Success){
                    livedata.value = Event(
                        ResourceState.Success(
                            PeopleListModel(
                                count = it.body.size+10,
                                results = it.body.map {
                                    PeopleItemModel(it)
                                }
                            )
                        )
                    )
                }
                if(it is ResourceState.Failure){
                    livedata.value = Event(ResourceState.Failure(it.exception))}
            }
            livedata
        }
    fun fetchOfflineCachePeopleList(){
        fetchOfflinePeopleListUseCase.setup(null)
    }

    val updateOfflinePeopleLiveData = updateOfflinePeopleListUseCase.currentData
    fun updateOfflineCachingPeople (content : List<PeopleItemResponse>) {
        updateOfflinePeopleListUseCase.setup(
            content
        )
    }

    fun onDestroy(){
        fetchPeopleUseCase.cancel()
        searchPeopleUseCase.cancel()
        fetchOfflinePeopleListUseCase.cancel()
        updateOfflinePeopleListUseCase.cancel()
    }
}