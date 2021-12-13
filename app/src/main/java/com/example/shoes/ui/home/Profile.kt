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

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoes.R
import com.example.shoes.model.Response
import com.example.shoes.model.User
import com.example.shoes.model.UserResponse
import com.example.shoes.ui.components.MyTextField
import com.example.shoes.ui.theme.ShoesTheme
import com.example.shoes.utils.Resource
import com.example.shoes.viewmodel.ShoesViewModel
import kotlinx.coroutines.launch

@Composable
fun Profile(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
    user: MutableState<User>,
) {
    if (user.value.ID != "") {
        ProfileScreen(user = user)
    } else {
        UserScreen(user = user)
    }
}

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    state: LoginState = rememberLoginState(),
    user: MutableState<User>,
) {
    val result = remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize(),

        ) {
        TopAppBar(
            title = {
            },

            navigationIcon = {
                // show drawer icon
                IconButton(
                    onClick = {
                        result.value = "Drawer icon clicked"
                    }
                ) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )
                }
            },

            actions = {
                Button(
                    onClick = {
                        state.login = true
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                ) {
                    Text(text = "Đăng nhập", color = Color.fromHex("#fed700"))
                }
                Button(
                    onClick = {
                        state.login = false
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.White),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.fromHex("#fed700")
                    )
                ) {
                    Text(text = "Đăng kí", color = Color.White)
                }
            },
            backgroundColor = Color.fromHex("#fed700"),
            elevation = AppBarDefaults.TopAppBarElevation
        )
        when (state.loginDisplay) {
            ProfileDisplay.Login -> Login(userLogin = user)
            else -> Register(state)
        }
    }
}

@Composable
fun Login(
    userAPI: ShoesViewModel = hiltViewModel(),
    userLogin: MutableState<User>,
) {
    val scope = rememberCoroutineScope()
    val user_name = remember { mutableStateOf("") };
    val login = remember { mutableStateOf(false) };
    val password = remember { mutableStateOf("") };
    val error = remember { mutableStateOf("") };
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!userAPI.isLoading.value && login.value == true) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                Image(
                    painter = painterResource(
                        id = R.drawable.logo
                    ),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    MyTextField(label = "User name", field = user_name, KeyboardType.Text);
                    MyTextField(label = "Password", field = password, KeyboardType.Password);
                    Text(text = error.value, color = Color.Red);
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.fromHex("#fed700")
                    ),
                    onClick = {
                        if (user_name.value == "") {
                            error.value = "User name cannot be empty"
                            login.value = false
                        } else
                            if (password.value == "") {
                                error.value = "Password cannot be empty"
                                login.value = false
                            } else {
                                login.value = true
                            }

                    }
                )
                {
                    Text(text = "Submit", color = Color.White)

                }
            }
        }
        if (login.value == true) {
            val user = produceState<Resource<UserResponse>>(initialValue = Resource.Loading()) {
                value = userAPI.login(user_name.value, password.value)
            }.value

            scope.launch {
                val result = userAPI.login(user_name.value, password.value)
                if (result is Resource.Success) {
                    Toast.makeText(context, "Fetching data success!", Toast.LENGTH_SHORT).show()
                } else if (result is Resource.Error) {
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            if (userAPI.isLoading.value) {
                user.data?.let {
                    userLogin.value = user.data.data
                }
            }

        }
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: LoginState = rememberLoginState(),
    user: MutableState<User>,
) {
    val result = remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = user.value.display_name,
                        color = Color.White
                    )
                    Text(
                        text = user.value.user_email,
                        color = Color.White
                    )
                }
            },
            navigationIcon = {
                // show drawer icon
                IconButton(
                    onClick = {
                        result.value = "Drawer icon clicked"
                    }
                ) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )
                }
            },

            actions = {
                var menuExpanded by remember { mutableStateOf(false) }

                IconButton(
                    onClick = {
                        menuExpanded = true
                    }
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Card(
                    shape = RectangleShape,
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        },
                        modifier = Modifier
                            .background(Color.fromHex("#fed700"))
                            .padding(10.dp),

                        ) {
                        DropdownMenuItem(
                            onClick = {},
                            modifier = Modifier.border(1.dp, color = Color.White)
                        ) {
                            IconButton(
                                onClick = {
                                    user.value = User()
                                }
                            ) {
                                Row {
                                    Icon(
                                        Icons.Filled.Logout,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        text = "Logout",
                                        color = Color.White
                                    )
                                }

                            }
                        }
                    }
                }
            },
            backgroundColor = Color.fromHex("#fed700"),
            elevation = AppBarDefaults.TopAppBarElevation
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),

            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(1.dp, color = Color.fromHex("#ECECEC"))
                        .fillMaxWidth(),
                    verticalAlignment = (Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Filled.ShoppingBag,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 10.dp),
                        tint = Color.fromHex("#fed700")
                    )
                    Text(
                        text = "Your Orders",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(1.dp, color = Color.fromHex("#ECECEC"))
                        .fillMaxWidth(),
                    verticalAlignment = (Alignment.CenterVertically),
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 10.dp),
                        tint = Color.fromHex("#fed700")
                    )
                    Text(
                        text = "Recommend to friends",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(1.dp, color = Color.fromHex("#ECECEC"))
                        .fillMaxWidth(),
                    verticalAlignment = (Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Default.ShoppingBasket,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 10.dp),
                        tint = Color.fromHex("#fed700")
                    )
                    Text(
                        text = "Voucher",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun rememberLoginState(
    login: Boolean = true,
): LoginState {
    return remember {
        LoginState(
            login = login,
        )
    }
}

@Stable
class LoginState(
    login: Boolean,
) {
    var login by mutableStateOf(login)
    val loginDisplay: ProfileDisplay
        get() {
            return when {
                login -> ProfileDisplay.Login
                else -> ProfileDisplay.Register
            }
        }
}


@Composable
fun Register(
    state: LoginState,
    userAPI: ShoesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val user_name = remember { mutableStateOf("") };
    val email = remember { mutableStateOf("") };
    val register = remember { mutableStateOf(false) };
    val password = remember { mutableStateOf("") };
    val error = remember { mutableStateOf("") };
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.logo
                    ),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                )
                if (!userAPI.isLoading.value && register.value == true && error.value=="") {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    MyTextField(label = "User name", field = user_name, KeyboardType.Text);
                    MyTextField(label = "Password", field = password, KeyboardType.Password);
                    MyTextField(label = "Email", field = email, KeyboardType.Email);
                    Text(
                        text = error.value,
                        color = Color.Red,
                        modifier=Modifier.padding(15.dp)
                    );
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.fromHex("#fed700")
                    ),
                    onClick = {
                        if (user_name.value == "") {
                            error.value = "User name cannot be empty"
                            register.value = false
                        } else
                            if (password.value == "") {
                                error.value = "Password cannot be empty"
                                register.value = false
                            } else
                                if (email.value == "") {
                                    error.value = "Email cannot be empty"
                                    register.value = false
                                } else {
                                    error.value = ""
                                    register.value = true
                                }

                    }
                )
                {
                    Text(text = "Submit", color = Color.White)

                }
            }
        }
        if (register.value == true) {
            val user = produceState<Resource<Response>>(initialValue = Resource.Loading()) {
                value = userAPI.register(user_name.value, password.value, email.value)
            }.value

            if (userAPI.isLoading.value) {
                user.data?.let {
                    if (user.data.code==200){
                        state.login=true
                    }
                }
            }
            if(user.message!=null){
                error.value="User name or password already exist!"
            }
        }
    }
}

enum class ProfileDisplay {
    Login, Register
}
