package com.example.testapplication.domain.usecases

import com.example.testapplication.base_component.base_classes.BaseUseCase
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import kotlinx.coroutines.launch
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemResponse

class FetchOfflinePeopleListUseCase(
    val myApi : SwapiApiInterface
) : BaseUseCase<Any?, List<PeopleItemResponse>>() {
    override fun setup(parameter: Any?) {
        super.setup(parameter)
        launch(coroutineContext) {
            execute {
                var data : List<PeopleItemResponse>? = null
                try {
                    data = myApi.getOfflinePeopleList()
                } catch (e : Throwable){
                    return@execute null
                }
                return@execute data
            }}
    }
}
