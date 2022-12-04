package com.example.testapplication.base_classes

import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.viewbinding.ViewBinding
import com.example.testapplication.base_component.base_classes.BaseAdapter
import com.example.testapplication.base_component.base_classes.BaseViewHolder
import com.example.testapplication.base_component.base_interface.BaseModel
import com.fasterxml.jackson.databind.ser.Serializers
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import java.io.Serializable


@RunWith(MockitoJUnitRunner::class)
internal class BaseAdapterTest{

    @Mock
    lateinit var mockViewBinding : ViewBinding
    @Mock
    lateinit var view : View
    @Mock
    lateinit var mockVH : BaseViewHolder<BaseModel>

    data class MockData(
        val string : String = "halo"
    ) : Serializable, BaseModel

    val data1 = MockData("data1")
    val data2 = MockData("data2")


    lateinit var SUT : BaseAdapter<BaseViewHolder<BaseModel>>
    var isBindVhCalled = false
    var bindPos = -1

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = object : BaseAdapter<BaseViewHolder<BaseModel>>(){

            override fun bindVH(holder: BaseViewHolder<BaseModel>, position: Int) {
                isBindVhCalled = true
                bindPos = position
            }
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BaseViewHolder<BaseModel> {
                return object : BaseViewHolder<BaseModel>(view){
                    override fun bindData(model: BaseModel) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    fun success(){
        // no op
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterCreation_checkParam_listItemZero() {
        // Arrange
        success()
        // Act
        // Assert
        assertThat(SUT.listItems.isEmpty(), `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterUpdateList_verify_content(){
        // Arrange

        // Act
        try{
            SUT.insertAtTop(data1)
        } catch(e : Throwable) { } //  do nothing
        // Assert
        assertThat(SUT.listItems.isEmpty(), `is`(false))
        assertThat(SUT.listItems[0], `is`(data1))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterUpdateList_updateList_contentCorrect(){
        // Arrange
        // Act
        try{
            SUT.insertAll(
                listOf(data1,data2)
            )
        } catch(e : Throwable) { } //  do nothing
        // Assert
        assertThat(SUT.listItems.isEmpty(), `is`(false))
        assertThat(SUT.listItems[0], `is`(data1))
        assertThat(SUT.listItems[1], `is`(data2))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterUpdateList_removeContent_contentCorrect(){
        // Arrange
        try{
            SUT.insertAll(
                listOf(data1,data2)
            )
        } catch(e : Throwable) { }
        // Act
        try{
            SUT.removeItem(data1)
        } catch(e : Throwable) { }
        // Assert
        assertThat(SUT.listItems.isEmpty(), `is`(false))
        assertThat(SUT.listItems[0], `is`(data2))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterUpdateList_clearList_contentCorrect(){
        // Arrange
        try{
            SUT.insertAll(
                listOf(data1,data2)
            )
        } catch(e : Throwable) { }
        // Act
        try{
            SUT.clearList()
        } catch(e : Throwable) { }
        // Assert
        assertThat(SUT.listItems.isEmpty(), `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun adapterUpdateList_getItemCount_contentCorrect(){
        // Arrange
        try{
            SUT.insertAll(
                listOf(data1,data2)
            )
        } catch(e : Throwable) { }
        // Act
        // Assert
        assertThat(SUT.getItemCount(), `is`(2))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun onBindViewHondler(){
        // Arrange
        try{
            SUT.insertAll(
                listOf(data1,data2)
            )
        } catch(e : Throwable) { }
        // Act
        SUT.onBindViewHolder(
            mockVH, 1
        )
        // Assert
        assertThat(isBindVhCalled, `is`(true))
        assertThat(bindPos, `is`(1))
    }

}