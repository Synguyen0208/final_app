package com.example.shoes.ui.home.cart

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.shoes.R
import com.example.shoes.model.OrderLine
import com.example.shoes.model.ShoesResponse
import com.example.shoes.model.SnackCollection
import com.example.shoes.model.ShoesRepo
import com.example.shoes.ui.components.ShoesButton
import com.example.shoes.ui.components.ShoesDivider
import com.example.shoes.ui.components.ShoesSurface
import com.example.shoes.ui.components.QuantitySelector
import com.example.shoes.ui.components.SnackImage
import com.example.shoes.ui.home.DestinationBar
import com.example.shoes.ui.rememberShoesAppState
import com.example.shoes.ui.theme.AlphaNearOpaque
import com.example.shoes.ui.theme.ShoesTheme
import com.example.shoes.ui.utils.formatPrice
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun Cart(
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    cart: CartViewModel
) {
    val orderLines by cart.orderLines.collectAsState()
    Cart(
        orderLines = orderLines,
        removeShoes = cart::removeShoes,
        addShoes = cart::addShoes,
        increaseItemCount = cart::increaseSnackCount,
        decreaseItemCount = cart::decreaseSnackCount,
        onShoesClick = onShoesClick,
        modifier = modifier
    )
}

@Composable
fun Cart(
    orderLines: List<OrderLine>,
    removeShoes: (Long) -> Unit,
    addShoes: (ShoesResponse) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val appState = rememberShoesAppState()
    ShoesSurface(modifier = modifier.fillMaxSize()) {
        Box {
            CartContent(
                orderLines = orderLines,
                removeShoes = removeShoes,
                addShoes = addShoes,
                increaseItemCount = increaseItemCount,
                decreaseItemCount = decreaseItemCount,
                onShoesClick = onShoesClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(
                navigateToRoute= appState::navigateToBottomBarRoute,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            CheckoutBar(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CartContent(
    orderLines: List<OrderLine>,
    removeShoes: (Long) -> Unit,
    addShoes: (ShoesResponse) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val snackCountFormattedString = remember(orderLines.size, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size, orderLines.size
        )
    }

    LazyColumn(modifier) {
        item {
            Spacer(Modifier.statusBarsHeight(additional = 56.dp))
            Text(
                text = stringResource(R.string.cart_order_header, snackCountFormattedString),
                style = MaterialTheme.typography.h6,
                color = ShoesTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        items(orderLines) { orderLine ->
            SwipeDismissItem(
                background = { offsetX ->
                    val backgroundColor = if (offsetX < -160.dp) {
                        ShoesTheme.colors.error
                    } else {
                        ShoesTheme.colors.uiFloated
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(backgroundColor),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val padding: Dp by animateDpAsState(
                            if (offsetX > -160.dp) 4.dp else 0.dp
                        )
                        Box(
                            Modifier
                                .width(offsetX * -1)
                                .padding(padding)
                        ) {
                            val height = (offsetX + 8.dp) * -1
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                color = ShoesTheme.colors.error
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (offsetX < -40.dp && offsetX > -152.dp) {
                                        val iconAlpha: Float by animateFloatAsState(
                                            if (offsetX < -120.dp) 0.5f else 1f
                                        )

                                        Icon(
                                            imageVector = Icons.Filled.DeleteForever,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .graphicsLayer(alpha = iconAlpha),
                                            tint = ShoesTheme.colors.uiBackground,
                                            contentDescription = null,
                                        )
                                    }
                                    val textAlpha by animateFloatAsState(
                                        if (offsetX > -144.dp) 0.5f else 1f
                                    )
                                    if (offsetX < -120.dp) {
                                        Text(
                                            text = stringResource(id = R.string.remove_item),
                                            style = MaterialTheme.typography.subtitle1,
                                            color = ShoesTheme.colors.uiBackground,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .graphicsLayer(
                                                    alpha = textAlpha
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            ) {
                CartItem(
                    orderLine = orderLine,
                    removeShoes = removeShoes,
                    increaseItemCount = increaseItemCount,
                    decreaseItemCount = decreaseItemCount,
                    onShoesClick = onShoesClick
                )
            }
        }
        item {
            SummaryItem(
                subtotal = orderLines.map { it.shoes.price.toLong() * it.count }.sum(),
                shippingCosts = 369
            )
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    removeShoes: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val shoes = orderLine.shoes
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShoesClick(shoes.id) }
            .background(ShoesTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)

    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        shoes.images?.get(0)?.let {
            SnackImage(
                imageUrl = it.src,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
            )
        }
        Text(
            text = shoes.name,
            style = MaterialTheme.typography.subtitle1,
            color = ShoesTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeShoes(shoes.id) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                tint = ShoesTheme.colors.iconSecondary,
                contentDescription = stringResource(R.string.label_remove)
            )
        }
        shoes.categories?.get(0)?.let {
            Text(
                text = it.name,
                style = MaterialTheme.typography.body1,
                color = ShoesTheme.colors.textHelp,
                modifier = Modifier.constrainAs(tag) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
            )
        }
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
        Text(
            text = shoes.price,
            style = MaterialTheme.typography.subtitle1,
            color = ShoesTheme.colors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = quantity.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        QuantitySelector(
            count = orderLine.count,
            decreaseItemCount = { decreaseItemCount(shoes.id) },
            increaseItemCount = { increaseItemCount(shoes.id) },
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            }
        )
        ShoesDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun SummaryItem(
    subtotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.cart_summary_header),
            style = MaterialTheme.typography.h6,
            color = ShoesTheme.colors.brand,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = stringResource(R.string.cart_subtotal_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.cart_shipping_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ShoesDivider()
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.cart_total_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .wrapContentWidth(Alignment.End)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal + shippingCosts),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        ShoesDivider()
    }
}

@Composable
private fun CheckoutBar(modifier: Modifier = Modifier) {
    Column(
        modifier.background(
            ShoesTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque)
        )
    ) {

        ShoesDivider()
        Row {
            Spacer(Modifier.weight(1f))
            ShoesButton(
                onClick = { /* todo */ },
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.cart_checkout),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
            }
        }
    }
}
