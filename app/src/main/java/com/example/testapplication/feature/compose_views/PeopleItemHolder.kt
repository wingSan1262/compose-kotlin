package com.example.testapplication.feature.compose_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.testapplication.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapplication.MyApplication.compose_themes.Black_3333
import com.example.testapplication.MyApplication.compose_themes.Black_808080
import com.example.testapplication.MyApplication.compose_themes.Gray_F5F5F5
import com.example.testapplication.MyApplication.compose_themes.TestApplicationTheme
import com.example.testapplication.feature.PeopleViewHolder
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel

@Composable
fun PeopleItemHolder(
    peopleItemModel : PeopleItemModel,
    onItemClick : () -> Unit = {}
){
    val item : PeopleItemModel = peopleItemModel

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .clickable {
                onItemClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_blob),
            contentDescription = "bg",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 14.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = item.name, color = Black_808080,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text( text = item.gender,
                    color = Black_808080,
                    style = TextStyle(fontSize = 12.sp))
            }
            Row(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .wrapContentHeight()
                ) {
                    Text( text = item.height,
                        color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                    Text( text = item.mass,
                        color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                    Text( text = item.birthYear,
                        color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                }
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .wrapContentHeight()
                ) {
                    Text( text = item.hairColor,
                            color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                    Text( text = item.skinColor,
                        color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                    Text( text = item.eyeColor,
                        color = Black_808080 ,style = TextStyle(fontSize = 11.sp))
                }
            }
        }
    }
}

@Composable
@Preview
private fun ViewItemPreview(){
    TestApplicationTheme {
        PeopleItemHolder(
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
            )
        )
    }
}