package com.example.testapplication.feature

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testapplication.MyApplication.compose_themes.TestApplicationTheme
import com.example.testapplication.base_component.base_classes.BaseActivity
import com.example.testapplication.base_component.entities.ResourceState
import com.example.testapplication.feature.compose_views.CustomSearchBarView
import com.example.testapplication.feature.compose_views.PeopleListWidget
import com.example.testapplication.feature.compose_views.SearchBar
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleListModel
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class MainActivity : BaseActivity() {

    @Inject
    lateinit var listViewModel: SearchViewModel

    private lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {

        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        observeNonComposeData()

        setContent {
            navController = rememberNavController()
            TestApplicationTheme {

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
                                        errorToast(listContent.exception.message.toString()) }
                                }
                            }

                        offlineListState.value?.nonFilteredContent()?.let { listContent ->
                            when(listContent){
                                is ResourceState.Success -> updateLazyList(listContent.body)
                                is ResourceState.Failure -> errorToast(listContent.exception.message.toString())
                            }
                        }
                    }
                )
            }
        }

        // initial search
        if(listViewModel.peopleListHolder.isEmpty())
            listViewModel.searchPeopleData(
                listViewModel.currentQuerySearch )
    }

    private fun observeNonComposeData() {
        listViewModel.updateOfflinePeopleLiveData.observe(this){
            it.contentIfNotHandled?.let {
                when(it){
                    is ResourceState.Failure ->
                        errorToast(it.exception.message.toString())
                }
            }
        }
    }

    fun errorToast(
        msg : String = "There is something wrong, please check network"
    ){
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_LONG
        ).show()
    }

    @Composable
    fun updateLazyList(listContent : PeopleListModel){
        listContent.results?.let{

            PeopleListWidget(
                listPeople = it,
                fetchNewPage = {
                    if(!listViewModel.isQuerying)
                        listViewModel.loadMorePage()
                } ) }

    }

    @Composable
    fun initView(
        listPeople: List<PeopleItemModel> = listOf()
    ){
        Scaffold(
            topBar = {
                SearchBar()
            },
            content = {
                PeopleListWidget(listPeople)
            }
        )
    }

    @Composable
    @Preview
    private fun previewView(){
        TestApplicationTheme {
            initView(listOf(PeopleItemModel(id=0), PeopleItemModel(id=1)))
        }}
}