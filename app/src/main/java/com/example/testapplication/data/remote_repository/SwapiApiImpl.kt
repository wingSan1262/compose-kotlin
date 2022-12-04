package com.example.testapplication.data.remote_repository

import com.example.testapplication.base_component.entities.ResourceState
import vanrrtech.app.ajaib_app_sample.data.SQDb.github.PeopleItemDao
import vanrrtech.app.ajaib_app_sample.data.remote_repository.SwapiRetrofitInterface
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.QueryPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemResponse
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.SearchPeopleResponse

class SwapiApiImpl(
    val swapiRetrofitInterface: SwapiRetrofitInterface,
    val dao: PeopleItemDao
) : SwapiApiInterface {

    override suspend fun getPeopleList(queryPeopleRequest: QueryPeopleRequest)
    : SearchPeopleResponse? {
        return try {swapiRetrofitInterface.getPeopleList(
            queryPeopleRequest.page
        )
        } catch (e : Throwable){
            null
        }
    }

    override suspend fun searchPeopleList(searchPeopleRequest: SearchPeopleRequest)
    : SearchPeopleResponse? {
        return try{
            swapiRetrofitInterface.searchPeople(
                searchPeopleRequest.query,
                searchPeopleRequest.page
            )
        } catch (e : Throwable){
            null
        }

    }

    override suspend fun getOfflinePeopleList(): List<PeopleItemResponse>? {
        return dao.loadAllUser()
    }

    override suspend fun setOfflineCachePeopleList(list: List<PeopleItemResponse>?): Boolean {
        try{
            dao.nukeTable()
            list?.forEach {
                dao.insertUsers(it)
            }
        } catch(e : Throwable){
            return false
        }
        return true
    }


}