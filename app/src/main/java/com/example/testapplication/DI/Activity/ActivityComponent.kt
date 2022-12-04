package com.example.testapplication.DI.Activity

import com.example.testapplication.feature.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(context: MainActivity)

}