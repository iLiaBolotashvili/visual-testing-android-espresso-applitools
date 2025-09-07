package com.example.shop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shop.data.Product
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay
import com.example.shop.ui.components.ShopTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    products: List<Product>,
    cartItems: List<Product>,
    onAddToCart: (Product) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var lastAdded by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(lastAdded) {
        lastAdded?.let {
            snackbarHostState.showSnackbar("Added $it")
            lastAdded = null
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            ShopTopBar(
                title = "Shop",
                cartCount = cartItems.size,
                onCartClick = onCartClick,
                onProfileClick = onProfileClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.id }) { product ->
                val isInCart = cartItems.any { it.id == product.id }

                ProductItemCard(
                    product = product,
                    isInCart = isInCart,
                    onAdd = {
                        onAddToCart(product)
                        lastAdded = product.name
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductItemCard(
    product: Product,
    isInCart: Boolean,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddedChip by remember { mutableStateOf(false) }
    LaunchedEffect(showAddedChip) {
        if (showAddedChip) {
            delay(1500)
            showAddedChip = false
        }
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    product.brand,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    product.price.asMoney(),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedVisibility(visible = showAddedChip, enter = fadeIn(), exit = fadeOut()) {
                        AssistChip(
                            onClick = { showAddedChip = false },
                            label = { Text("Added") },
                            leadingIcon = { Icon(Icons.Filled.Check, contentDescription = null) }
                        )
                    }

                    if (showAddedChip) Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onAdd()
                            showAddedChip = true
                        },
                        enabled = !isInCart,
                        modifier = Modifier.testTag("add_${product.id}"),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(if (isInCart) "In Cart" else "Add")
                    }
                }
            }
        }
    }
}

private fun Double.asMoney(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)
