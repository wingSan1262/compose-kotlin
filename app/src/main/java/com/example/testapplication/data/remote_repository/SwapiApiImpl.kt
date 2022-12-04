package com.example.testapplication.data.remote_repository

import com.example.testapplication.data.local_db.PeopleItemDao
import com.example.testapplication.domain.models.request.QueryPeopleRequest
import com.example.testapplication.domain.models.request.SearchPeopleRequest
import com.example.testapplication.domain.models.response.PeopleItemResponse
import com.example.testapplication.domain.models.response.SearchPeopleResponse

/**
 * class that handle all SwapiApi call
 * Add all new api related to Swapi here
 * also dont forget add the
 * @see SwapiApiInterface
 * @see SwapiRetrofitInterface
 * @see RemoteApiRetrofitClient
 */
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