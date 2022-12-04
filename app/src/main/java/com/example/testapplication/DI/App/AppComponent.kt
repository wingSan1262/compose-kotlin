package com.example.testapplication.DI.App

import androidx.compose.ui.ExperimentalComposeUiApi
import dagger.Component
import com.example.testapplication.DI.Activity.ActivityComponent
import com.example.testapplication.DI.Activity.ActivityModule

@AppScope
@Component(modules = [
    AppModule::class,
    UseCasesModules::class
])
interface AppComponent {
    @OptIn(ExperimentalComposeUiApi::class)
    fun newActivityComponent (activityModule: ActivityModule) : ActivityComponent
}