package com.example.shop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shop.data.Product
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay

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
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Shop", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                    BadgedBox(
                        modifier = Modifier.padding(end = 10.dp),
                        badge = {
                            val count = cartItems.size
                            if (count > 0) Badge { Text(count.coerceAtMost(99).toString()) }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
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
    modifier: Modifier = Modifier
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
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = product.price.asMoney(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(visible = showAddedChip, enter = fadeIn(), exit = fadeOut()) {
                    AssistChip(
                        onClick = { showAddedChip = false },
                        label = { Text("Added") },
                        leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) }
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(if (isInCart) "In Cart" else "Add")
                }
            }
        }
    }
}

private fun Double.asMoney(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)
