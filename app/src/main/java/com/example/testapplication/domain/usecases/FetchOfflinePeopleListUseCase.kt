package com.example.testapplication.domain.usecases

import com.example.testapplication.base_component.base_classes.BaseUseCase
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import kotlinx.coroutines.launch
import com.example.testapplication.domain.models.response.PeopleItemResponse

/**
 * API usecase wrapper
 * please subscribe to this livedata usecase
 *
 * @see BaseUseCase
 */
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
