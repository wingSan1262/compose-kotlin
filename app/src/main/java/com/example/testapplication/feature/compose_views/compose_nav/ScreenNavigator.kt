package com.example.testapplication.feature.compose_views.compose_nav

import androidx.navigation.NavHostController

// same as when we set graph and action id
class ScreenNavigator(
    val nav : NavHostController
){
    companion object{
        val LIST_ROOT_ROUTE = "list"

        val DETAIL_SCREEN_ROOT = "detail"
        val DETAIL_ARGUMENT_ID = listOf(
            "index_or_id"
        )
        val DETAIL_SCREEN_ROUTE : String get() {
            var path = DETAIL_SCREEN_ROOT
            for(arguments in DETAIL_ARGUMENT_ID){
                path += "/{$arguments}"
            }
            return path
        }
    }
    fun navigateToListScreen (isPop : Boolean = false) {
        nav.navigate(
            route = LIST_ROOT_ROUTE
        ){ if(isPop) popUpTo(LIST_ROOT_ROUTE) }
    }

    fun navigateToDetailScreen (
        indexOrId : Int,
        isPop : Boolean = false,
    ) {
        nav.navigate(
            route = getArgument(DETAIL_SCREEN_ROOT, listOf(indexOrId.toString()))
        ){ if(isPop) popUpTo(DETAIL_SCREEN_ROUTE) }
    }

    private fun getArgument(
        root : String,
        args : List<String>,
    ): String {
        var path = root
        for(arguments in args){
            path += "/$arguments"
        }
        return path
    }
}