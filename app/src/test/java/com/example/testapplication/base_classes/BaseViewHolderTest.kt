package com.example.testapplication.base_classes

import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.viewbinding.ViewBinding
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
import java.io.Serializable


@RunWith(MockitoJUnitRunner::class)
internal class BaseViewHolderTest{

    var SUT : BaseViewHolder<BaseModel>? = null
    data class MockData(
        val string : String = "halo"
    ) : Serializable, BaseModel
    @Mock
    lateinit var mockVb : ViewBinding
    @Mock
    lateinit var root : View

    var isBinded = false
    @Before
    @Throws(Exception::class)
    fun setup() {
        isBinded = false
        SUT = object : BaseViewHolder<BaseModel>(root){
            override fun bindData(model: BaseModel) {
                isBinded = true
            }
        }
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun viewHolderBind_success_bindedTrue() {
        SUT?.bindData(MockData())
        assertThat(isBinded, `is`(true))
    }
    @Test
    @Throws(java.lang.Exception::class)
    fun viewHolderBind_success_bindedFalse() {
        assertThat(isBinded, `is`(false))
    }
}