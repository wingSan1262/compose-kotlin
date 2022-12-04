package com.example.testapplication.domain.usecases

import com.example.testapplication.base_component.base_classes.BaseUseCase
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import kotlinx.coroutines.launch
import com.example.testapplication.domain.models.request.QueryPeopleRequest
import com.example.testapplication.domain.models.response.SearchPeopleResponse

/**
 * API usecase wrapper
 * please subscribe to this livedata usecase
 *
 * @see BaseUseCase
 */
class FetchPeopleUseCase(
    val api : SwapiApiInterface
) : BaseUseCase<QueryPeopleRequest, SearchPeopleResponse>(){

    override fun setup(parameter: QueryPeopleRequest) {
        super.setup(parameter)
        launch(coroutineContext) {
            execute {
                var data : SearchPeopleResponse? = null
                api.getPeopleList(parameter).let{
                    data = it
                    return@execute data
                }
            }
        }
    }
}