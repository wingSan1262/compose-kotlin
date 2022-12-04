package com.example.testapplication.DI.App

import dagger.Component
import com.example.testapplication.DI.Activity.ActivityComponent
import com.example.testapplication.DI.Activity.ActivityModule


/**
 * this DI schema is following good practice on Android Basic Scoping
 * which actually already same with Dagger Hilt (note : still use Dagger 2)
 *
 * All component that needs to stay on AppScope
 * or stay when app start until killed, unchanged
 * use AppModule and Scoped,
 *
 * if i may please avoid Singleton, due to its one of anti pattern
 * and will make testing harder . . . Appscoping can be one of its alternates
 *
 * if need for activity scoped object, please add there
 * @See ActivityComponent
 *
 * @Link https://www.youtube.com/watch?v=sZVXFgpte68
 */
@AppScope
@Component(modules = [
    AppModule::class,
    UseCasesModules::class
])
interface AppComponent {
    fun newActivityComponent (activityModule: ActivityModule) : ActivityComponent
}