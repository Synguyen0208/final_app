package com.example.shoes.ui.shoesdetail

import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoes.R
import com.example.shoes.model.*
import com.example.shoes.ui.components.*
import com.example.shoes.ui.home.cart.CartViewModel
import com.example.shoes.ui.home.fromHex
import com.example.shoes.ui.theme.ShoesTheme
import com.example.shoes.ui.theme.Neutral8
import com.example.shoes.ui.utils.mirroringBackIcon
import com.example.shoes.utils.Resource
import com.example.shoes.viewmodel.ShoesViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@ExperimentalMaterialApi
@Composable
fun ShoesDetail(
    shoesId: Long,
    upPress: () -> Unit,
    shoeRepo: ShoesViewModel = hiltViewModel(),
    cart: CartViewModel,
) {
    val text:String="";
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val shoes = produceState<Resource<ShoesResponse>>(initialValue = Resource.Loading()) {
        value = shoeRepo.getShoeData(shoesId)
    }.value
    val scroll = rememberScrollState(0)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Green)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                    ,
                ) {
                    FlowRow(
                        mainAxisAlignment = MainAxisAlignment.Center,
                        mainAxisSize = SizeMode.Expand,
                        crossAxisSpacing = 8.dp,
                        mainAxisSpacing = 8.dp
                    ) {
                        val options=shoes.data?.attributes?.get(0)?.options;
                        if (options != null) {
                            options.forEach { shoe ->
                                    Row(
                                        Modifier
                                            .width(78.dp)
                                            .selectable(
                                                selected = (text == shoe),
                                                onClick = {

                                                })
                                    ) {
                                        RadioButton(
                                            modifier = Modifier
                                                .padding(10.dp),
                                            selected = (text == shoe),
                                            onClick = {
//                                                onClickListener(text)
                                            })
                                        Text(
                                            text = shoe,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                        )
                                    }

                            }
                        }
                    }
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ShoesSurface(modifier = Modifier.fillMaxSize()) {
                if (!shoeRepo.isLoading.value) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                scope.launch {
                    val result = shoeRepo.getShoeData(shoesId)

                    if (result is Resource.Success) {
                        Toast.makeText(context, "Fetching data success!", Toast.LENGTH_SHORT).show()
                    } else if (result is Resource.Error) {
                        Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (shoeRepo.isLoading.value) {
                        if (shoes is Resource.Success) {
                            shoesView(shoes = shoes, upPress = upPress, scroll = scroll)
                            CartBottomBar(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                addCart = cart::addShoes,
                                shoes = shoes,
                                openBottom =bottomSheetScaffoldState
                            )
                        }
                    }
                    Up(upPress = upPress)
                }
                shoes.message?.let { it1 -> Text(text = shoes.message) }
            }

        }
    }

}

@Composable
fun shoesView(
    shoes: Resource<ShoesResponse>,
    upPress: () -> Unit,
    scroll: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        Box(
        ) {

            Column {
                shoes.data?.images?.let {
                    Image(imageUrl = shoes.data.images)
                }
            }
        }
        Title(shoes = shoes)
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    Column(modifier = Modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = Color.fromHex("#fed700"),
            contentColor = ShoesTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            IconButton(
                onClick = upPress,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(36.dp)
                    .background(
                        color = Neutral8.copy(alpha = 0.32f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = mirroringBackIcon(),
                    tint = ShoesTheme.colors.iconInteractive,
                    contentDescription = stringResource(R.string.label_back),
                    modifier = Modifier.width(34.dp)
                )

            }
        }
        ShoesDivider()
    }
}

@Composable
private fun Title(
    shoes: Resource<ShoesResponse>,
) {
    Column(
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .fillMaxWidth()
            .padding(bottom = 30.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        shoes.data?.name?.let {
            Text(
                text = shoes.data.name,
                style = MaterialTheme.typography.h6,
                color = Color.fromHex("#fed700"),
                modifier = HzPadding
            )
        }

        Spacer(Modifier.height(4.dp))
        shoes.data?.price?.let {
            Text(
                text = shoes.data.price + " VNĐ",
                style = MaterialTheme.typography.h6,
                color = ShoesTheme.colors.textPrimary,
                modifier = HzPadding
            )
        }
        ShoesDivider()
        Spacer(Modifier.height(4.dp))
        shoes.data?.description?.let {
            AndroidView(
                modifier = Modifier
                    .padding(10.dp),
                factory = { context ->
                    TextView(
                        context,
                    ).apply {
                        text = Html.fromHtml(shoes.data.description)
                    }
                })
        }

    }
}

@Composable
private fun Image(
    imageUrl: Array<Image>,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(imageUrl) { image ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.fromHex("#FFFFCC"))
            ) {
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                SnackImage1(
                    imageUrl = image.src,
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }


}

@ExperimentalMaterialApi
@Composable
private fun CartBottomBar(
    modifier: Modifier = Modifier,
    addCart: (ShoesResponse) -> Unit,
    shoes: Resource<ShoesResponse>,
    openBottom: BottomSheetScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    val (count, updateCount) = remember { mutableStateOf(1) }
    ShoesSurface(modifier) {
        Column {
            ShoesDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding(start = false, end = false)
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
            ) {
//                ShoesButton(
//                    onClick = {
//                        coroutineScope.launch {
//
//                            if (openBottom.bottomSheetState.isCollapsed){
//                                openBottom.bottomSheetState.expand()
//                            }else{
//                                openBottom.bottomSheetState.collapse()
//                            }
//                        }
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = stringResource(R.string.add_to_cart),
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        maxLines = 1
//                    )
//                }
                Spacer(Modifier.width(16.dp))
                ShoesButton(
                    onClick = { shoes.data?.let { addCart(it) } },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.add_to_cart),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
