@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.tzh.sneakerland.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.tzh.sneakarland.ui.theme.OnPrimaryColor
import com.tzh.sneakarland.ui.theme.PrimaryColor
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakarland.util.AnimatedKeyExtension
import com.tzh.sneakerland.screen.detail.component.AnimatedContainerDropdownBox
import com.tzh.sneakerland.screen.detail.component.ColorCircleRow
import com.tzh.sneakerland.screen.detail.component.QuantitySelector
import com.tzh.sneakerland.ui.theme.SnakerLandTheme
import com.tzh.sneakerland.util.Extension.showToast
import com.tzh.sneakerland.util.Gender
import kotlinx.coroutines.Dispatchers

@Composable
fun DetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    sneakerModel: SneakerModel,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.setSneaker(sneakerModel)
    }
    BackHandler { onBackPress() }
    val mSneakerModel =
        viewModel.currentSneaker.collectAsStateWithLifecycle().value ?: SneakerModel()
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailImage(
                mSneakerModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                animateModifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        AnimatedKeyExtension.getImageKey(
                            mSneakerModel.id.toString()
                        ),
                    ), animatedVisibilityScope = animatedContentScope
                ),
                onBackPress = onBackPress,
                onFavouriteClick = {
                    viewModel.updateFavourite(
                        mSneakerModel.copy(
                            isFavourite = !mSneakerModel.isFavourite
                        )
                    )
                },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val genderShoeText = when (mSneakerModel.gender) {
                    Gender.MALE -> "Men's Shoes"
                    Gender.FEMALE -> "Women's Shoes"
                    Gender.UNISEX -> "Unisex Shoes"
                }
                if (mSneakerModel.gender == Gender.MALE) "Man's Shoes" else "Woman's Shoes"
                Text(
                    text = genderShoeText, style = MaterialTheme.typography.bodySmall.copy(
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.6f
                        )
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "(${mSneakerModel.rating.formatIfWhole()})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.6f
                            )
                        )
                    )
                }
            }
            //TitleText
            TitleRow(mSneakerModel, animatedContentScope)
            SizeSelectorContent(
                Modifier.fillMaxWidth(), selectedValue = mSneakerModel.selectedSize
            ) {
                mSneakerModel.selectedSize = it
            }
            AnimatedContainerDropdownBox(
                "Description",
                content = {
                    Text(
                        mSneakerModel.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(0.5f)
                    )
                },
            )
            AnimatedContainerDropdownBox(
                "Free Delivery and Returns",
                content = {
                    Text(
                        mSneakerModel.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(0.5f)
                    )
                },
            )
            ColorCircleRow(mSneakerModel.selectedColor) {
                mSneakerModel.selectedColor = it
            }
            QuantitySelector(mSneakerModel.currentQty) {
                mSneakerModel.currentQty = it
            }
            AddCartButton() {
                viewModel.addCart(onLoading = {
                    isLoading = it
                }, onSuccessMessage = {
                    context.showToast(it)
                    onBackPress()
                }, onShowMessage = {
                    context.showToast(it)
                })
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.TitleRow(
    sneakerModel: SneakerModel, animatedContentScope: AnimatedContentScope
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            sneakerModel.name, style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp, fontWeight = FontWeight.Bold
            ), modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState(
                        AnimatedKeyExtension.getTitleKey(
                            sneakerModel.name
                        )
                    ), animatedContentScope
                )
                .weight(1f)
        )
        Text(
            "$" + sneakerModel.price, style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun AddCartButton(addCart: () -> Unit) {
    ElevatedButton(
        onClick = addCart,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text("Add To Cart", modifier = Modifier.padding(vertical = 12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SizeSelectorContent(
    modifier: Modifier, selectedValue: Double? = 5.5, selectedSize: (Double) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Size:", style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "US", style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "UK", style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Eu", style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {

            items(sneakerSizes.size) {
                val color = if (selectedValue == sneakerSizes[it]) {
                    CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black
                    )
                } else {
                    CardDefaults.elevatedCardColors()
                }

                ElevatedCard(
                    onClick = { selectedSize(sneakerSizes[it]) }, colors = color
                ) {
                    Text(
                        sneakerSizes[it].formatIfWhole(),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

val sneakerSizes = List(15) { 5 + it * 0.5 }
fun Double.formatIfWhole(): String {
    return if (this % 1 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}

@Composable
fun DetailImage(
    sneakerModel: SneakerModel,
    modifier: Modifier,
    animateModifier: Modifier,
    onBackPress: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    val context = LocalContext.current
    Box(modifier) {
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )

        val imageRequest =
            ImageRequest.Builder(context).data(sneakerModel.image).dispatcher(Dispatchers.IO)
                .crossfade(true).diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED).build()
        AsyncImage(
            model = imageRequest,
            contentDescription = sneakerModel.image,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .then(animateModifier),
            contentScale = ContentScale.Fit,
        )

        IconButton(
            onClick = onBackPress,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .border(
                    1.dp, color = Color.Black.copy(
                        alpha = 0.4f
                    ), shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",

                )
        }
        IconButton(
            onClick = onFavouriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .border(
                    1.dp, color = Color.Black.copy(
                        alpha = 0.4f
                    ), shape = CircleShape
                )

        ) {
            Icon(
                imageVector = if (sneakerModel.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (sneakerModel.isFavourite) PrimaryColor else OnPrimaryColor,

                )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    SnakerLandTheme {
        SharedTransitionLayout() {
            AnimatedContent(targetState = true) {
                if (it) {
                    DetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        SneakerModel()
                    ) {

                    }
                }
            }
        }
    }
}