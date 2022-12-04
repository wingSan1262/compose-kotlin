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
class UpdateOfflinePeopleListUseCase(
    val myApi : SwapiApiInterface
) : BaseUseCase<List<PeopleItemResponse>, Boolean>() {
    override fun setup(parameter: List<PeopleItemResponse>) {
        super.setup(parameter)
        launch(coroutineContext) {
            var data : Boolean = false
            execute {
                try {
                    data = myApi.setOfflineCachePeopleList(parameter)
                } catch (e : Throwable){
                    return@execute false
                }
                return@execute data
            }}
    }
}
