package com.example.testapplication.DI.Activity

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.testapplication.feature.MainActivity
import com.example.testapplication.feature.MainActivityComposeNav
import dagger.Subcomponent

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(context: MainActivity)
    fun inject(context: MainActivityComposeNav)

}