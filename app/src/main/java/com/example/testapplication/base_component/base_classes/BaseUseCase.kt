package com.example.testapplication.base_component.base_classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import com.example.testapplication.base_component.abstracts.UseCase
import com.example.testapplication.base_component.entities.Event
import com.example.testapplication.base_component.entities.ResourceState
import kotlin.coroutines.CoroutineContext

open class BaseUseCase<PARAM, RESULT> : CoroutineScope, UseCase() {

    /** UseCase Original Response LiveData**/
    private val _currentData = MutableLiveData<Event<ResourceState<RESULT>>>()
    val currentData : LiveData<Event<ResourceState<RESULT>>> = _currentData

    /** Job Context Scope **/
    protected var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    /** Execute UseCase api or process **/
    fun execute(runApi : suspend () -> RESULT?){
        launch(coroutineContext){
            val res = try {
                val data = runApi()
                if(data != null)
                    ResourceState.Success(data as RESULT) else
                    ResourceState.Failure(Throwable("There is something wrong in the network"))
            } catch (e : Throwable){
                ResourceState.Failure(
                    Throwable("There is something wrong in the network"))
            }
            withContext(Dispatchers.Main){
                _currentData.postValue(Event(res))
            }
        }
    }

    /** Open function for user class to varies the execute call **/
    open fun setup(parameter: PARAM){ checkJob() }
    /** Common Job Control**/
    fun cancel() { job.cancel() }
    fun isCancelled(): Boolean { return job.isCancelled }
    fun checkJob(){
        if(job.isCancelled)
            job = Job() }
}