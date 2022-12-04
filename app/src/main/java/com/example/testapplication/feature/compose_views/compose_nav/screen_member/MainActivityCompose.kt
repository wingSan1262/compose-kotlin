package com.example.testapplication.feature.compose_views.compose_nav.screen_member

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testapplication.MyApplication.compose_themes.TestApplicationTheme
import com.example.testapplication.base_component.entities.ResourceState
import com.example.testapplication.feature.SearchViewModel
import com.example.testapplication.feature.compose_views.CustomSearchBarView
import com.example.testapplication.feature.compose_views.PeopleListWidget
import com.example.testapplication.feature.compose_views.compose_nav.ScreenNavigator.Companion.DETAIL_ARGUMENT_ID
import com.example.testapplication.feature.compose_views.compose_nav.ScreenNavigator.Companion.DETAIL_SCREEN_ROUTE
import com.example.testapplication.feature.compose_views.compose_nav.ScreenNavigator.Companion.LIST_ROOT_ROUTE
import kotlinx.coroutines.delay
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleListModel

@ExperimentalComposeUiApi
fun NavGraphBuilder.listScreenCompose(
// declare here callback that can direct to which screen
// here you go this is your nav graph in declarative UI
    listViewModel : SearchViewModel,
    navigateToDetailScreen :  (Int, isPop : Boolean) -> Unit,
){
    @Composable
    fun updateLazyList(listContent : PeopleListModel){
        listContent.results?.let{ it ->
            PeopleListWidget(
                listPeople = it,
                fetchNewPage = {
                    if(!listViewModel.isQuerying)
                        listViewModel.loadMorePage()
                },
                { index ->  navigateToDetailScreen(index, false)}) }
    }


    composable(
        route = LIST_ROOT_ROUTE
    ){

        val context = LocalContext.current
        val listState = listViewModel.searchPeopleLiveData.observeAsState()
        val offlineListState = listViewModel.offlineCachingLiveData.observeAsState()
        Scaffold(
            topBar = {
                CustomSearchBarView(
                    onTextChange = { // start fresh searching
                        listViewModel.searchPeopleData(it) },
                    onCloseClicked = { // reset searching
                        listViewModel.currentQuerySearch = ""
                        listViewModel.searchPeopleData("") } ) },

            content = {
                listState.value
                    ?.nonFilteredContent()?.let{ listContent ->
                        when(listContent){
                            is ResourceState.Success -> updateLazyList(listContent.body)
                            is ResourceState.Failure -> {
                                listViewModel.fetchOfflineCachePeopleList()
                                errorToast(context, listContent.exception.message.toString()) }
                        } }
                offlineListState.value?.nonFilteredContent()?.let { listContent ->
                    when(listContent){
                        is ResourceState.Success -> updateLazyList(listContent.body)
                        is ResourceState.Failure -> errorToast(context, listContent.exception.message.toString())
                    } }
            }
        )

        // initial composition actions
        LaunchedEffect(true){
            delay(300)
            if(listViewModel.peopleListHolder.isEmpty())
                listViewModel.searchPeopleData(
                    listViewModel.currentQuerySearch
                )
        }
    }
}

fun errorToast(
    context : Context,
    msg : String = "There is something wrong, please check network",
){
    Toast.makeText(
        context,
        msg,
        Toast.LENGTH_LONG
    ).show()
}

