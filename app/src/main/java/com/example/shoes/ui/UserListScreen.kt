package com.example.shoes.ui

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.shoes.R
import com.example.shoes.model.ShoesResponse

@Composable
@OptIn(ExperimentalCoilApi::class)
fun UserListItem(shoesResponse: ShoesResponse) {

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = shoesResponse.id.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.padding(10.dp))
            AndroidView(factory = { context ->
                TextView(context).apply {
                    text = Html.fromHtml(shoesResponse.description)
                }
            })
            Image(
                painter = rememberImagePainter(
                    data = "https://sh-store.xyz/wp-content/uploads/2021/10/gia%CC%80y-air-force-1-07-essential-cz0270-103-king-shoes-sneaker-real-hcm-2-600x750.jpeg",
                    builder = {
                        crossfade(true)
                        placeholder(drawableResId = R.drawable.banner1)
                    }
                ),
                contentDescription = "kk",
                contentScale = ContentScale.Crop,
            )
//            Text(
//                text = "Title: ${shoesResponse.description}",
//                modifier = Modifier.fillMaxWidth(),
//                fontSize = 15.sp,
//                fontWeight = FontWeight.Normal,
//                fontFamily = FontFamily.SansSerif
//            )

            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                text = "Status: ${shoesResponse.name}",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}


















