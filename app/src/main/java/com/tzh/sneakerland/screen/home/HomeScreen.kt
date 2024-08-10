package com.tzh.sneakerland.screen.home


import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.tzh.sneakarland.ui.theme.BackgroundColor
import com.tzh.sneakarland.ui.theme.OnPrimaryColor
import com.tzh.sneakarland.ui.theme.PrimaryColor
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakarland.util.AnimatedKeyExtension
import com.tzh.sneakerland.R
import com.tzh.sneakerland.util.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onSneakerClick: (SneakerModel) -> Unit,
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val lazylistState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.hamburger),
                    contentDescription = "Menu",
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.shopping_bag),
                    contentDescription = "ShoppingBag",
                    modifier = Modifier.size(28.dp)
                )
            }

        }
        SearchBar(query = uiState.query, onQueryChange = {
            homeViewModel.onEvent(
                HomeUIEvent.SearchName(it)
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        BrandRow(
            brandList = uiState.brandList, onSelectedBrand = {
                homeViewModel.onEvent(
                    HomeUIEvent.FilterBrand(it)
                )
            }, selectedValue = uiState.selectedBrand
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Popular", style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                Box(contentAlignment = Alignment.CenterEnd) {
                    Box(
                        Modifier
                            .width(100.dp)
                            .menuAnchor(), contentAlignment = Alignment.CenterEnd
                    ) {

                    }

                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(
                            imageVector = Icons.Default.List, "FilterType"
                        )
                    }
                }

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    FilterType.entries.forEach {
                        DropdownMenuItem(text = { Text(text = it.name) }, onClick = {
                            expanded = false
                            homeViewModel.onEvent(
                                HomeUIEvent.FilterByType(it)
                            )
                            scope.launch {
                                lazylistState.animateScrollToItem(0)
                            }
                        })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SneakerList(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            sharedTransitionScope,
            animatedContentScope,
            lazylistState,
            onSneakerClick = {
                homeViewModel.setSneaker(it)
                onSneakerClick(it)
            },
            sneakerList = uiState.sneakerList,
            updateFavourite = homeViewModel::updateFavourite
        )
    }
}

@Composable
fun SearchBar(
    query: String, onQueryChange: (String) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ), shape = CircleShape, modifier = Modifier.scale(scaleY = 0.8f, scaleX = 1f)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            },
            singleLine = true, maxLines = 1,
            placeholder = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                unfocusedLabelColor = Color.Gray.copy(alpha = 0.7f),
                unfocusedLeadingIconColor = Color.Gray.copy(alpha = 0.7f)
            ),
            shape = CircleShape
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandRow(
    brandList: List<String>, selectedValue: String, onSelectedBrand: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(brandList, key = { it }) {
            // Determine if the current sneaker model's brand is selected
            val isSelected = selectedValue == it

            ElevatedCard(
                onClick = { onSelectedBrand(it) }, shape = RoundedCornerShape(8.dp),

                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 8.dp
                ), modifier = Modifier.size(width = 80.dp, 55.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    BrandLogo(it)

                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White.copy(
                            alpha = 0.5f
                        ) else Color.Gray.copy(
                            alpha = 0.4f
                        )
                    )
                    Surface(color = containerColor, modifier = Modifier.fillMaxSize()) {

                    }
                }
            }
        }
    }
}

@Composable
fun BrandLogo(brand: String) {
    val logo = remember {
        when (brand.lowercase()) {
            "nike" -> R.drawable.nike
            "vans" -> R.drawable.vans
            "adidas" -> R.drawable.adidas
            "puma" -> R.drawable.puma
            "reebok" -> R.drawable.reebok
            "new balance" -> R.drawable.new_balance
            "converse" -> R.drawable.converse
            else -> R.drawable.nike // Fallback resource
        }
    }
// Use the drawable resource ID
    Image(
        painter = painterResource(id = logo),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
        // Adjust size as needed,
        ,
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SneakerList(
    modifier: Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    lazyListState: LazyListState,
    onSneakerClick: (SneakerModel) -> Unit,
    sneakerList: List<SneakerModel>,
    updateFavourite: (SneakerModel) -> Unit,
) {
    if (sneakerList.isNotEmpty()) {

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            state = lazyListState
        ) {
            items(sneakerList, key = { it.id }) {
                with(sharedTransitionScope) {
                    SneakerItem(
                        it,
                        modifier = Modifier.animateItem(),
                        imageModifier = Modifier.sharedElement(
                            state = rememberSharedContentState(AnimatedKeyExtension.getImageKey(it.id.toString())),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        titleModifier = Modifier.sharedElement(
                            state = rememberSharedContentState(AnimatedKeyExtension.getTitleKey(it.name)),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        onClick = { onSneakerClick(it) },
                        updateFavourite = updateFavourite
                    )
                }
            }
        }
    }

}

@Composable
fun SneakerItem(
    sneakerModel: SneakerModel,
    modifier: Modifier,
    imageModifier: Modifier,
    titleModifier: Modifier,
    onClick: () -> Unit,
    updateFavourite: (SneakerModel) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .shadow(
                8.dp, shape = RoundedCornerShape(8.dp), spotColor = Color.Gray.copy(alpha = 0.8f)
            )
            .background(BackgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$" + sneakerModel.price,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                val imageRequest = ImageRequest.Builder(context).data(sneakerModel.image)
                    .dispatcher(Dispatchers.IO).crossfade(true).diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED).build()
                AsyncImage(
                    model = imageRequest,
                    contentDescription = sneakerModel.image,
                    modifier = imageModifier
                        .height(120.dp)
                        .weight(1f),
                    contentScale = ContentScale.Inside,
                )
                IconButton(
                    onClick = {
                        updateFavourite(sneakerModel)
                    },
                ) {
                    Icon(
                        imageVector = if (sneakerModel.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (sneakerModel.isFavourite) PrimaryColor else OnPrimaryColor
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sneakerModel.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = titleModifier.weight(1f)
                )

                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Favorite"
                    )
                }
            }
        }
    }
}
