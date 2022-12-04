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
import com.example.testapplication.feature.compose_views.compose_nav.ComposeNavigation
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleListModel
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class MainActivityComposeNav : BaseActivity() {

    @Inject
    lateinit var listViewModel: SearchViewModel

    private lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {

        activityComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            TestApplicationTheme {
                ComposeNavigation(
                    nav = navController,
                    listViewModel = listViewModel
                )
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
}