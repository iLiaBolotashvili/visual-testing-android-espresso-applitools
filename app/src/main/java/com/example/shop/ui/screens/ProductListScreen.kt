package com.example.shop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shop.data.Product
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    products: List<Product>,
    onAddToCart: (Product) -> Unit,
    onCartClick: () -> Unit,
    cartCount: Int,
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
                    BadgedBox(
                        modifier = Modifier.padding(end = 10.dp),
                        badge = {
                            if (cartCount > 0) Badge { Text(cartCount.coerceAtMost(99).toString()) }
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
                ProductItemCard(
                    product = product,
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
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var added by remember { mutableStateOf(false) }

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

            Column(horizontalAlignment = Alignment.End) {
                Button(
                    onClick = {
                        onAdd()
                        added = true
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Add")
                }

                AnimatedVisibility(
                    visible = added,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AssistChip(
                        onClick = { added = false },
                        label = { Text("Added!") },
                        leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

private fun Double.asMoney(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)
