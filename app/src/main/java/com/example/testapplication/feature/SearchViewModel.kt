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
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.QueryPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemResponse
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleListModel

class SearchViewModel(
    val fetchPeopleUseCase: FetchPeopleUseCase,
    val searchPeopleUseCase: SearchPeopleUseCase,
    val fetchOfflinePeopleListUseCase: FetchOfflinePeopleListUseCase,
    val updateOfflinePeopleListUseCase: UpdateOfflinePeopleListUseCase
) : ViewModel(){

    var isQuerying = false
    var maxCount = 0
    var currentPage = 1
    var currentQuerySearch = ""

    var peopleListHolder = ArrayList<PeopleItemModel>()

    val searchPeopleLiveData = Transformations.switchMap(searchPeopleUseCase.currentData){
        val livedata = MutableLiveData<Event<ResourceState<PeopleListModel>>>()

        it.contentIfNotHandled?.let {
            if(it is ResourceState.Success){

                if(currentPage == 1){
                    it.body.results?.let { it1 -> updateOfflineCachingPeople(it1) }
                    peopleListHolder.clear()
                }

                PeopleListModel(it.body).let { respModel ->
                    respModel.results?.let { list ->
                        peopleListHolder.addAll(list)
                    }
                    maxCount = respModel.count
                    livedata.value = Event(
                        ResourceState.Success(
                            respModel.apply { results = peopleListHolder }
                        ))
                }
            }

            if(it is ResourceState.Failure){
                livedata.value = Event(ResourceState.Failure(it.exception))}

        }

        isQuerying = false
        livedata
    }


    fun searchPeopleData (
        querySearch : String = "",
        isLoadMore : Boolean = false
    ){
        if(!isLoadMore) currentPage = 1

        isQuerying = true
        currentQuerySearch = querySearch

        searchPeopleUseCase.setup(
            SearchPeopleRequest(
                currentQuerySearch,
                currentPage
            )
        )
    }

    fun loadMorePage() {
        if(peopleListHolder.size == maxCount) return
        currentPage ++
        searchPeopleData(currentQuerySearch, true)
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