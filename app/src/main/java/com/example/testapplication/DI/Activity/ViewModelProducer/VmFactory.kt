package com.example.testapplication.DI.Activity.ViewModelProducer

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.example.testapplication.domain.usecases.FetchOfflinePeopleListUseCase
import com.example.testapplication.domain.usecases.FetchPeopleUseCase
import com.example.testapplication.domain.usecases.SearchPeopleUseCase
import com.example.testapplication.domain.usecases.UpdateOfflinePeopleListUseCase
import com.example.testapplication.feature.SearchViewModel

/**
 * standard ViewModel factory
 * TODO remove this to dagger ViewModel bindkey feature
 */
class VmFactory(
    val mApplication: Application,
    val fetchPeopleUseCase: FetchPeopleUseCase,
    val searchPeopleUseCase: SearchPeopleUseCase,
    val fetchOfflinePeopleListUseCase: FetchOfflinePeopleListUseCase,
    val updateOfflinePeopleListUseCase: UpdateOfflinePeopleListUseCase
) : ViewModelProvider.Factory {

    fun <T : ViewModel?> getClassInstance(modelClass: Class<T>): T {
        when(modelClass){
            SearchViewModel::class.java -> {
                return modelClass.getConstructor(
                    FetchPeopleUseCase::class.java,
                    SearchPeopleUseCase::class.java,
                    FetchOfflinePeopleListUseCase::class.java,
                    UpdateOfflinePeopleListUseCase::class.java
                ).newInstance(
                    fetchPeopleUseCase,
                    searchPeopleUseCase,
                    fetchOfflinePeopleListUseCase,
                    updateOfflinePeopleListUseCase
                ) as T
            }
            else -> {
                throw Exception("no vm found")
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return getClassInstance(modelClass)
    }

}