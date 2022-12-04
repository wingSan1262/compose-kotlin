package com.example.testapplication.feature.compose_views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapplication.MyApplication.compose_themes.Transparent
import com.example.testapplication.MyApplication.compose_themes.White
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel

@Composable
fun PeopleListWidget(
    listPeople : List<PeopleItemModel>,
    fetchNewPage : ()->Unit = {},
    onClickItem : (index : Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    val endOfListReached by remember { derivedStateOf {  scrollState.isScrolledToEnd() }}

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = White,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 6.dp,
                bottom = 6.dp
            )
            .border(2.dp, Transparent),
    ){
        LazyColumn(
            state = scrollState
        ){
            items(
                listPeople,
                key = {
                    listPeople.indexOf(it)
                }
            ){
                PeopleItemHolder(
                    it
                ) { onClickItem(listPeople.indexOf(it)) }
            }
        }

        LaunchedEffect(endOfListReached) {
            if(endOfListReached){
                fetchNewPage()
            }
        }
    }
}

fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1