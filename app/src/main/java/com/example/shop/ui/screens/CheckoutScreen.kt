package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shop.data.Product
import com.example.shop.ui.glitch.LocalGlitch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<Product>,
    onPlaceOrder: () -> Unit,
    onBack: () -> Unit
) {
    val glitch = LocalGlitch.current

    val subtotal = remember(cartItems) { cartItems.sumOf { it.price } }
    val shipping = if (subtotal > 0) 4.99 else 0.0
    val total = subtotal + shipping

    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }

    val canPlace = cartItems.isNotEmpty() && fullName.isNotBlank() && address.isNotBlank()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Order Summary", style = MaterialTheme.typography.titleMedium)

                    if (cartItems.isEmpty()) {
                        Text(
                            "Your cart is empty.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val preview = cartItems.take(3)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            preview.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = item.name,
                                        // BUG #1: slight distortion
                                        contentScale = if (glitch) ContentScale.FillBounds else ContentScale.Crop,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            item.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1
                                        )
                                        Text(
                                            item.brand,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                    Text(item.price.asMoney(), style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            val extra = cartItems.size - preview.size
                            if (extra > 0) {
                                Text(
                                    "+$extra more item${if (extra > 1) "s" else ""}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // BUG #2: divider goes missing
                        if (!glitch) {
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                        }

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(subtotal.asMoney())
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Shipping", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(shipping.asMoney())
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "Total",
                                // BUG #3: total looks less prominent
                                style = if (glitch) MaterialTheme.typography.bodyLarge
                                else MaterialTheme.typography.titleSmall
                            )
                            Text(
                                total.asMoney(),
                                style = if (glitch) MaterialTheme.typography.titleSmall
                                else MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Shipping & Payment", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = card,
                        onValueChange = { card = it },
                        label = { Text("Card (**** **** **** 1234)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onPlaceOrder,
                enabled = canPlace,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Place order")
            }
        }
    }
}

private fun Double.asMoney(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)
