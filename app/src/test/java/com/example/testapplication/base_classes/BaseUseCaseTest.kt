package com.example.testapplication.base_classes

import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testapplication.base_component.base_classes.BaseUseCase
import com.example.testapplication.base_component.base_interface.BaseModel
import com.example.testapplication.base_component.entities.ResourceState
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.Serializable

@RunWith(MockitoJUnitRunner::class)
internal class BaseUseCaseTest{

    data class MockParam(
        val string : String = "halo"
    ) : Serializable, BaseModel
    data class MockResult(
        val string : String = "halo"
    ) : Serializable, BaseModel

    lateinit var SUT : BaseUseCase<MockParam, MockResult>

    val dispatcher = TestCoroutineDispatcher()
    var setupCalled = false

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setup() {
        Dispatchers.setMain(dispatcher)
        SUT = object : BaseUseCase<MockParam, MockResult>(){
            override fun setup(parameter: MockParam) {
                setupCalled = true
                super.setup(parameter)
            }
        }
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    @Throws(java.lang.Exception::class)
    fun callApi_success_correctDataNotified() {
        // Arrange
        var data : ResourceState<MockResult>? = null
        val result = MockResult("")
        // Act

        runTest {
            SUT.execute {
                result
            }
            advanceUntilIdle()
        }

        SUT.currentData.observeForever{
            data = it.contentIfNotHandled
        }

        // Assert
        if(data is ResourceState.Success){
            assertThat((data as ResourceState.Success<MockResult>).body, `is`(result))
        } else {
            throw Throwable("data api fail")
        }
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun callApi_fail_ReturnFail() {
        // Arrange
        var data : ResourceState<MockResult>? = null
        // Act

        SUT.currentData.observeForever{
            data = it.contentIfNotHandled
        }

        runTest {
            SUT.execute {
                return@execute null
            }
            advanceUntilIdle()
        }

        // Assert
        val check = data is ResourceState.Failure?
        assertThat(check, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun callSetup_success_correctDataNotified() {
        // Arrange
        // Act
        SUT.setup(MockParam())
        // Assert
        assertThat(setupCalled, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun callSetupAndCanel_success_jobStateCorrect() {
        // Arrange
        // Act
        SUT.setup(MockParam())
        // Assert
        assertThat(setupCalled, `is`(true))
        SUT.cancel()
        assertThat(SUT.isCancelled(), `is`(true))
        SUT.setup(MockParam())
        assertThat(SUT.isCancelled(), `is`(false))
    }
}