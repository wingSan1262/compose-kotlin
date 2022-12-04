package com.example.testapplication.base_component.base_classes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.testapplication.DI.Activity.ActivityComponent
import com.example.testapplication.DI.Activity.ActivityModule
import com.example.testapplication.DI.Activity.ViewBinderFactory.ViewBinderFactory
import com.example.testapplication.DI.App.AppComponent
import com.example.testapplication.MyApplication.MyApplication
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    /** Scoping injection tools **/
    val appComponent : AppComponent by lazy { (application as MyApplication).myAppComponent}
    val activityComponent : ActivityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this, this))
    }

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

    /**
     * helper for accessing binding component directly
     */
    fun withBinding(block: (VB.() -> Unit)): VB{
        val bindingAfterRunning: VB = viewBinding.apply { block.invoke(this) }
        return bindingAfterRunning
    }

}