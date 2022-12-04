package com.example.testapplication.base_component.base_classes

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.testapplication.DI.Activity.ActivityComponent
import com.example.testapplication.DI.Activity.ActivityModule
import com.example.testapplication.DI.App.AppComponent
import com.example.testapplication.MyApplication.MyApplication

@ExperimentalComposeUiApi
class BaseComposeActivity : ComponentActivity() {

    /** Scoping injection tools **/
    val appComponent : AppComponent by lazy { (application as MyApplication).myAppComponent}
    val activityComponent : ActivityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this as AppCompatActivity, this))
    }

}