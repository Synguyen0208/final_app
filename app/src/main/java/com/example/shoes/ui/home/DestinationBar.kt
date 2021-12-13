/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shoes.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoes.ui.components.ShoesDivider
import com.example.shoes.ui.theme.ShoesTheme
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun DestinationBar(
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = Color.fromHex("#fed700"),
            contentColor = ShoesTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Row(
                Modifier
                    .padding(16.dp)
                    .height(55.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Searching...", fontSize = 11.sp) },
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
                IconButton(onClick = {
                    navigateToRoute("home/cart")
                }) {
                    Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "", tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "", tint = Color.White)
                }
            }
        }
        ShoesDivider()
    }
}

