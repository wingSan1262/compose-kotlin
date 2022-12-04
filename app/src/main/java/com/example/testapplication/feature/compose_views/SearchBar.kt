package com.example.testapplication.feature.compose_views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapplication.MyApplication.compose_themes.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SearchBar() { CustomSearchBarView() }

@ExperimentalComposeUiApi
@Composable
fun CustomSearchBarView(
    onTextChange: (String) -> Unit = {},
    onCloseClicked : () -> Unit = {},
) {

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember {
        mutableStateOf("Search")
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()
    val debounceState = MutableStateFlow<String?>(null).apply {
        coroutineScope.launch{
            debounce(700)
                .distinctUntilChanged()
                .collect {
                    onTextChange(it ?: "")
                }
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = White,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_SIZE)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 2.dp
            )
            .border(2.dp, Transparent),
    ){
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    coroutineScope.launch {
                        debounceState.emit(it)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged {
                        if (it.hasFocus && !isSearching){
                            searchText = ""
                            isSearching = true
                        }
                    },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    disabledIndicatorColor = Transparent
                ),
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        "menu",
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1F)
                    )
                },
                trailingIcon = {
                    if(isSearching){
                        Icon(
                            Icons.Filled.Close,
                            "close",
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clickable(onClick = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()

                                    onCloseClicked()

                                    isSearching = false
                                    searchText = "Search"
                                }),
                        )
                    }
                }
            )
        }
    }
}


interface TextChangeListener{
    fun onTextChange() : String
}

@ExperimentalComposeUiApi
@Composable
@Preview
private fun SearchBarPreview(){
    TestApplicationTheme {
        SearchBar()
    }
}