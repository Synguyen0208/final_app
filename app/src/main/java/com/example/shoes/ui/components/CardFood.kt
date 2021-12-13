package com.example.shoes.ui.components


import android.annotation.SuppressLint
import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.foodapp.ui.components.FoodCard
import com.example.shoes.R
import com.example.shoes.model.ShoesResponse
import com.example.shoes.ui.home.fromHex

@SuppressLint("ResourceAsColor")
@Composable
public fun FoodItem(
    shoesResponse: ShoesResponse,
    modifier: Modifier = Modifier,
    onShoesClick: (Long) -> Unit,
) {
    FoodCard(
        modifier = modifier
            .padding(bottom = 6.dp, start = 6.dp)
            .width(180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .clickable(onClick = { onShoesClick(shoesResponse.id) })
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(color = Color.fromHex("#FFFFCC"))
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                shoesResponse.images?.get(1)?.let {
                    SnackImage1(
                        imageUrl = it.src,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            shoesResponse.categories?.get(0)?.let {
                Text(
                    text = it.name,
                    maxLines = 1,
                    color = Color.fromHex("#CC9933"),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Text(
                text = shoesResponse.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            AndroidView(
                modifier=Modifier
                    .padding(10.dp)
                ,

                factory = { context ->
                TextView(
                    context,
                ).apply {
                    text = Html.fromHtml(shoesResponse.price_html)
                    maxLines=1
                    setTextColor(R.color.price)
                }
            })
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SnackImage1(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    ShoesSurface(
        color = Color.LightGray,
        elevation = elevation,
        modifier = modifier
    ) {

        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(drawableResId = R.drawable.banner1)
                }
            ),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
//                .clip(RoundedCornerShape(10.dp))
            ,
            contentScale = ContentScale.Crop,

        )
    }
}