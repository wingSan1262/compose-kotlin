package com.example.testapplication.domain.usecases

import com.example.testapplication.base_component.base_classes.BaseUseCase
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import kotlinx.coroutines.launch
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.QueryPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.SearchPeopleResponse

class SearchPeopleUseCase(
    val api : SwapiApiInterface
) : BaseUseCase<SearchPeopleRequest, SearchPeopleResponse>(){

    override fun setup(parameter: SearchPeopleRequest) {
        super.setup(parameter)
        launch(coroutineContext) {
            execute {
                var data : SearchPeopleResponse? = null
                api.searchPeopleList(parameter).let{
                    data = it
                    return@execute data
                }
            }
        }
    }
}