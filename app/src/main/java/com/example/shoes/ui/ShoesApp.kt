package com.example.shoes.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.shoes.model.User
import com.example.shoes.ui.components.ShoesScaffold
import com.example.shoes.ui.home.HomeSections
import com.example.shoes.ui.home.ShoesBottomBar
import com.example.shoes.ui.home.addHomeGraph
import com.example.shoes.ui.home.cart.CartViewModel
import com.example.shoes.ui.shoesdetail.ShoesDetail
import com.example.shoes.ui.theme.ShoesTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
@ExperimentalMaterialApi
fun ShoesApp(
    viewModel: CartViewModel = viewModel(factory = CartViewModel.provideFactory()),
) {
    val user = remember { mutableStateOf(User()) };
    ProvideWindowInsets {
        ShoesTheme {
            val appState = rememberShoesAppState()
            ShoesScaffold(
                bottomBar = {
                    if (appState.shouldShowBottomBar) {
                        ShoesBottomBar(
                            tabs = appState.bottomBarTabs,
                            currentRoute = appState.currentRoute!!,
                            navigateToRoute = appState::navigateToBottomBarRoute
                        )
                    }
                },
                snackbarHost = {

                },
                scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    ShoesNavGraph(
                        onSnackSelected = appState::navigateToSnackDetail,
                        onLogin =appState::navigateToLogin,
                        upPress = appState::upPress,
                        cart =viewModel,
                        user =user
                    )
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
private fun NavGraphBuilder.ShoesNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    onLogin: (NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    cart: CartViewModel,
    user: MutableState<User>
) {

    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected,onLogin, cart =cart, user=user)
    }
    composable(
        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        Column{
            ShoesDetail(snackId, upPress, cart=cart)
        }

    }
    composable(
        "login"
    ) {
        Column{
            Text(text = "Login page")
        }
    }
}
