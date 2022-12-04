package com.example.testapplication.feature.compose_views.compose_nav

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.testapplication.feature.SearchViewModel
import com.example.testapplication.feature.compose_views.compose_nav.screen_member.detailScreen
import com.example.testapplication.feature.compose_views.compose_nav.screen_member.detailScreenCompose
import com.example.testapplication.feature.compose_views.compose_nav.screen_member.listScreenCompose


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ComposeNavigation(
    nav : NavHostController,
    listViewModel : SearchViewModel
){

    val screens = remember(nav){
        ScreenNavigator(nav)
    }

    NavHost(
        nav,
        startDestination = ScreenNavigator.LIST_ROOT_ROUTE
    ){
        listScreenCompose(
            listViewModel
        ){ id, isPop ->
            screens.navigateToDetailScreen(id) }

        detailScreenCompose(
            listViewModel){
            screens.navigateToListScreen(
                true
            ) }
    }

}