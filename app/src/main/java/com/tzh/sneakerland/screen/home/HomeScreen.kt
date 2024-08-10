package com.tzh.sneakerland.screen.home


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tzh.sneakarland.ui.theme.BackgroundColor
import com.tzh.sneakarland.ui.theme.OnPrimaryColor
import com.tzh.sneakarland.ui.theme.PrimaryColor
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.data.model.dummySneakerList
import com.tzh.sneakerland.ui.component.SneakerImage
import com.tzh.sneakarland.util.AnimatedKeyExtension
import com.tzh.sneakerland.ui.theme.SnakerLandTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onSneakerClick: (SneakerModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar()
        Spacer(modifier = Modifier.height(16.dp))
        Categories()
        Spacer(modifier = Modifier.height(16.dp))
        SneakerList(
            modifier = Modifier.weight(1f),
            sharedTransitionScope,
            animatedContentScope,
            onSneakerClick
        )
    }
}

@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = CircleShape
    ) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            },
            placeholder = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            ),
            shape = CircleShape
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Categories() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dummySneakerList) {
            Card(
                onClick = {},
                shape = CircleShape,
            ) {
                Text(text = it.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SneakerList(
    modifier: Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onSneakerClick: (SneakerModel) -> Unit
) {
    with(sharedTransitionScope) {
        LazyColumn(modifier = modifier) {
            items(dummySneakerList, key = { it.id }) {
                SneakerItem(
                    it,
                    imageModifier = Modifier.sharedElement(
                        state = rememberSharedContentState(AnimatedKeyExtension.getImageKey(it.image.toString())),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    titleModifier = Modifier.sharedElement(
                        state = rememberSharedContentState(AnimatedKeyExtension.getTitleKey(it.name)),
                        animatedVisibilityScope = animatedContentScope
                    ),
                ) {
                    onSneakerClick(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SneakerItem(
    sneakerModel: SneakerModel,
    imageModifier: Modifier,
    titleModifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundColor.copy(alpha = 0.4f),
            contentColor = BackgroundColor.copy(alpha = 0.5f)
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = sneakerModel.price,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                SneakerImage(
                    sneakerModel.image,
                    imageModifier
                        .height(100.dp)
                        .weight(1f)
                )

                IconButton(
                    onClick = {
                        sneakerModel.isFavourite = !sneakerModel.isFavourite
                    },
                ) {
                    Icon(
                        imageVector = if (sneakerModel.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (sneakerModel.isFavourite) PrimaryColor else OnPrimaryColor
                    )
                }
            }

            Row() {
                Text(
                    text = sneakerModel.name,
                    fontSize = 18.sp,
                    color = OnPrimaryColor,
                    modifier = titleModifier.weight(1f)
                )

                IconButton(
                    onClick = {

                    }, colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Favorite"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SnakerLandTheme {
        SharedTransitionLayout() {
            AnimatedContent(targetState = true) {
                if (it) {
                    HomeScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this
                    ) {}
                }
            }
        }
    }
}