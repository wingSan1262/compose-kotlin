package com.example.testapplication.feature.compose_views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapplication.MyApplication.compose_themes.TOP_APP_BAR_SIZE
import com.example.testapplication.MyApplication.compose_themes.TestApplicationTheme
import com.example.testapplication.MyApplication.compose_themes.Transparent
import com.example.testapplication.MyApplication.compose_themes.White
import com.example.testapplication.feature.PeopleViewHolder
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel

@ExperimentalComposeUiApi
@Composable
fun MainActivityScreenCompose(
    _isQuerying : Boolean?,
    _maxCount : Int?,
    _currentPage : Int?,
    _currentQuerySearch : String?,
    _listPeople : List<PeopleItemModel>,

) {

    val isQuerying = _isQuerying
    val maxCount = _maxCount
    val currentPage = _currentPage
    val currentQuerySearch = _currentQuerySearch
    val listPeople = _listPeople

    Scaffold(
        topBar = {
            SearchBar()
        },
        content = {
            PeopleListWidget(listPeople)
        }
    )
}

@ExperimentalComposeUiApi
@Composable
@Preview(showBackground = true)
fun MainActivityScreenComposePreview(){
    TestApplicationTheme {
        MainActivityScreenCompose(
            false,
            0,
            0,
            "",
            listOf(
                PeopleItemModel(
                    99,
                    "Wing",
                    "170",
                    "75",
                    "Black",
                    "brown",
                    "brown",
                    "1997",
                    "Male",
                ),
                PeopleItemModel(
                    100,
                    "Aini",
                    "170",
                    "75",
                    "Black",
                    "nigga",
                    "brown",
                    "1997",
                    "Male",
                )
            )
        )
    }
}


