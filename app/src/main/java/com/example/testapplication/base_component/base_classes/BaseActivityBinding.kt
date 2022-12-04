package com.example.testapplication.base_component.base_classes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.viewbinding.ViewBinding
import com.example.testapplication.DI.Activity.ActivityComponent
import com.example.testapplication.DI.Activity.ActivityModule
import com.example.testapplication.DI.Activity.ViewBinderFactory.ViewBinderFactory
import com.example.testapplication.DI.App.AppComponent
import com.example.testapplication.MyApplication.MyApplication
import javax.inject.Inject

@ExperimentalComposeUiApi
abstract class BaseActivityBinding<VB : ViewBinding> : BaseActivity() {

    /**
     * Common Injection Service Component, add here if needed
     */
    @Inject lateinit var viewBinderFactory: ViewBinderFactory

    /** Common View Binding Operation **/
    lateinit var viewBinding : VB
    fun <T>bindThisView(host: T, layoutInflater: LayoutInflater, container: ViewGroup?){
        viewBinderFactory.bindViewActivity(host!!::class.java, this, layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    fun withBinding(block: (VB.() -> Unit)): VB{
        val bindingAfterRunning: VB = viewBinding.apply { block.invoke(this) }
        return bindingAfterRunning
    }

}