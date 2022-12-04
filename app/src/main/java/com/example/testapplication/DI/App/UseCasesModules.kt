package com.example.testapplication.DI.App

import com.example.testapplication.data.remote_repository.SwapiApiInterface
import com.example.testapplication.domain.usecases.FetchOfflinePeopleListUseCase
import com.example.testapplication.domain.usecases.FetchPeopleUseCase
import com.example.testapplication.domain.usecases.SearchPeopleUseCase
import com.example.testapplication.domain.usecases.UpdateOfflinePeopleListUseCase
import dagger.Module
import dagger.Provides


/**
 * UseCase Module
 * Which AppScoped
 *
 * i think usecase is better to be available on all app or module
 * if you have better idea please modify
 *
 * @see AppComponent
 */
@Module
class UseCasesModules() {

    @Provides
    @AppScope
    fun getPeopleList(myApi : SwapiApiInterface) : FetchPeopleUseCase =
        FetchPeopleUseCase(myApi)

    @Provides
    @AppScope
    fun searchPeopleList(myApi : SwapiApiInterface) : SearchPeopleUseCase =
        SearchPeopleUseCase(myApi)

    @Provides
    @AppScope
    fun updateCachePeopleList(myApi : SwapiApiInterface) : UpdateOfflinePeopleListUseCase =
        UpdateOfflinePeopleListUseCase(myApi)

    @Provides
    @AppScope
    fun getCachePeopleList(myApi : SwapiApiInterface) : FetchOfflinePeopleListUseCase =
        FetchOfflinePeopleListUseCase(myApi)

}