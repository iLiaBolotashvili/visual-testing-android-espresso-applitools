package com.example.shop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shop.data.Product
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<Product>,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val subtotal = cartItems.sumOf { it.price }
    val tax = subtotal * 0.10
    val shipping = if (subtotal >= 100.0) 0.0 else if (subtotal == 0.0) 0.0 else 4.99
    val total = subtotal + tax + shipping

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Your Cart", style = MaterialTheme.typography.titleLarge)
                        if (cartItems.isNotEmpty()) {
                            Text(
                                "${cartItems.size} item${if (cartItems.size == 1) "" else "s"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(
                            total.money(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Button(
                        onClick = onCheckout,
                        enabled = cartItems.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text("Proceed to checkout")
                    }
                }
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            EmptyCartState(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                onBrowse = onBack
            )
        } else {
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item -> CartItemCard(item) }
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryRow("Subtotal", subtotal.money())
                        SummaryRow("Tax (10%)", tax.money())
                        SummaryRow("Shipping", if (shipping == 0.0) "Free" else shipping.money())

                        Divider(Modifier.padding(top = 4.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", style = MaterialTheme.typography.titleMedium)
                            Text(
                                total.money(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(84.dp))
            }
        }
    }
}

@Composable
private fun EmptyCartState(modifier: Modifier = Modifier, onBrowse: () -> Unit) {
    Box(modifier) {
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text("Your cart is empty", style = MaterialTheme.typography.titleMedium)
            Text(
                "Browse products and add items to your cart.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(onClick = onBrowse) { Text("Browse products") }
        }
    }
}

@Composable
private fun CartItemCard(item: Product) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            ),
                            tileMode = TileMode.Clamp
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    item.price.money(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AssistChip(onClick = {}, label = { Text("x1") }, enabled = false)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

private fun Double.money(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(max(this, 0.0))
