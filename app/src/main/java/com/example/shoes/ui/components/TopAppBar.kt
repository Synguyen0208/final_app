package com.example.shoes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoes.R

@Composable
fun TopAppBar(){
    Column {
        Row(
            Modifier
                .padding(16.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Search Food, Vegetable, etc.", fontSize = 12.sp) },
                singleLine = true,
                leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "", tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "", tint = Color.White)
            }
        }
        Card(
            Modifier
                .height(64.dp)
                .padding(horizontal = 16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QrButton()

                VerticalDivider()
                Row(Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { }
                    .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_money), contentDescription = "", tint = Color(0xFF6FCF97))
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "$120", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Top Up", color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    }
                }

                VerticalDivider()
                Row(Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { }
                    .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_coin), contentDescription = "", tint = MaterialTheme.colors.primary)
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "$10", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Points", color = Color.LightGray, fontSize = 12.sp)
                    }
                }
            }
        }
        Card(
            Modifier
                .height(64.dp)
                .padding(horizontal = 16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QrButton()

                VerticalDivider()
                Row(Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { }
                    .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_money), contentDescription = "", tint = Color(0xFF6FCF97))
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "$120", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Top Up", color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    }
                }

                VerticalDivider()
                Row(Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { }
                    .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_coin), contentDescription = "", tint = MaterialTheme.colors.primary)
                    Column(Modifier.padding(8.dp)) {
                        Text(text = "$10", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Points", color = Color.LightGray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun QrButton() {
    IconButton(
        onClick = {},
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_scan),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
@Composable
fun VerticalDivider() {
    Divider(
        color = Color(0xFFF1F1F1),
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
    )
}
