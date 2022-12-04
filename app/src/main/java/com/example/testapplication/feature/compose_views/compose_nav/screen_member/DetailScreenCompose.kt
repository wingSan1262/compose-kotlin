package com.example.testapplication.feature.compose_views.compose_nav.screen_member

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testapplication.MyApplication.compose_themes.*
import com.example.testapplication.feature.SearchViewModel
import com.example.testapplication.feature.compose_views.compose_nav.ScreenNavigator.Companion.DETAIL_ARGUMENT_ID
import com.example.testapplication.feature.compose_views.compose_nav.ScreenNavigator.Companion.DETAIL_SCREEN_ROUTE
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel

@ExperimentalMaterialApi
fun NavGraphBuilder.detailScreenCompose(
    listViewModel : SearchViewModel,
    navigateToListScreen : () -> Unit = {}
){

    composable(
        route = DETAIL_SCREEN_ROUTE,
        arguments = listOf(
            navArgument(DETAIL_ARGUMENT_ID[0]){
                type = NavType.IntType
            } )
    ){
        var stateIndex by remember {
            mutableStateOf(
                it.arguments?.getInt(DETAIL_ARGUMENT_ID[0]) ?: -1
            )
        }
        var data by remember {
            mutableStateOf(
                listViewModel.peopleListHolder.get(stateIndex)
            )
        }

        detailScreen(
            data,
            navigateToListScreen
        )
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun detailScreen(
    inputData : PeopleItemModel = PeopleItemModel(
        -1,
        "wing",
        "170",
        "72",
        "black",
        "brown",
        "brown",
        "1997",
        "male",
    ),
    exit : () -> Unit = {},
){
    BackHandler {
        exit()
    }
    var bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)
    var dataState by remember {
        mutableStateOf(inputData)
    }

    LaunchedEffect(bottomSheetState){
        exit()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    dataState.name,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(Purple200)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Column(
                    modifier = Modifier
                        .background(White)
                        .padding(horizontal = 18.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ){
                    Text(
                        "General Information",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 3.dp)
                    )
                    Text(text = "Character id " + dataState.id,
                        modifier = Modifier.padding(top = 2.dp))
                    Text(text = "born on " + dataState.birthYear,
                        modifier = Modifier.padding(top = 2.dp))
                    Text(text = "height " + dataState.height +" cm",
                        modifier = Modifier.padding(top = 2.dp))

                    Text(
                        "Character Information",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 3.dp)
                    )
                    Text(text = dataState.hairColor + " Hair",
                        modifier = Modifier.padding(top = 2.dp))
                    Text(text = dataState.eyeColor + " Eye",
                        modifier = Modifier.padding(top = 2.dp))
                    Text(text = dataState.skinColor + " Skin",
                        modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
    ){}

}
