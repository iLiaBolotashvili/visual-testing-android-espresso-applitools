package com.example.shop.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shop.data.Product
import com.example.shop.ui.glitch.LocalGlitch
import com.example.shop.ui.screens.CartScreen
import com.example.shop.ui.screens.CheckoutScreen
import com.example.shop.ui.screens.LoginScreen
import com.example.shop.ui.screens.ProductListScreen
import com.example.shop.ui.screens.ProfileScreen
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object ProductList : Screen("product_list")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object Profile : Screen("profile")
}

@Composable
fun ShopNavGraph(navController: NavHostController) {
    val products = remember {
        listOf(
            Product(1, "MacBook Pro 14”", "Apple", 1999.0, "https://picsum.photos/seed/laptop1/640/480",
                "M3 performance with Liquid Retina XDR. Great for dev and creative workloads."),
            Product(2, "Noise Cancelling Headphones", "Bose", 299.0, "https://picsum.photos/seed/headphones1/640/480",
                "Comfortable over-ear design with world-class ANC for travel and focus."),
            Product(3, "USB-C Charger 67W", "Anker", 39.99, "https://picsum.photos/seed/charger1/640/480",
                "Compact GaN fast charger for laptops, tablets, and phones."),
            Product(4, "Mechanical Keyboard TKL", "Keychron", 129.0, "https://picsum.photos/seed/keyboard1/640/480",
                "Hot-swappable switches, RGB backlight, and durable PBT keycaps."),
            Product(5, "Galaxy S24 Ultra", "Samsung", 1199.0, "https://picsum.photos/seed/phone1/640/480",
                "Flagship smartphone with incredible camera and bright AMOLED display."),
            Product(6, "iPad Air 11”", "Apple", 699.0, "https://picsum.photos/seed/tablet1/640/480",
                "Lightweight tablet with M2 chip and Apple Pencil support."),
            Product(7, "Smartwatch GT4", "Huawei", 249.0, "https://picsum.photos/seed/watch1/640/480",
                "Stylish smartwatch with health tracking and 2-week battery life."),
            Product(8, "Surface Laptop Go 3", "Microsoft", 899.0, "https://picsum.photos/seed/laptop2/640/480",
                "Slim, lightweight laptop perfect for students and everyday use."),
            Product(9, "AirPods Pro 2", "Apple", 249.0, "https://picsum.photos/seed/earbuds1/640/480",
                "Wireless earbuds with adaptive noise cancellation and spatial audio."),
            Product(10, "PlayStation 5", "Sony", 499.0, "https://picsum.photos/seed/console1/640/480",
                "Next-gen gaming console with stunning graphics and exclusive titles."),
            Product(11, "4K UltraWide Monitor", "LG", 699.0, "https://picsum.photos/seed/monitor1/640/480",
                "34-inch ultrawide monitor with HDR10 and razor-thin bezels."),
            Product(12, "Portable Bluetooth Speaker", "JBL", 149.0, "https://picsum.photos/seed/speaker1/640/480",
                "Rugged, waterproof speaker with deep bass and 12-hour battery life.")
        )
    }

    val cart = remember { mutableStateListOf<Product>() }
    val loggedInEmail = "demo@shop.com"

    var glitchEnabled by rememberSaveable { mutableStateOf(false) }

    CompositionLocalProvider(LocalGlitch provides glitchEnabled) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.ProductList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    glitchEnabled = glitchEnabled,
                    onToggleGlitch = { glitchEnabled = it }
                )
            }

            composable(Screen.ProductList.route) {
                ProductListScreen(
                    products = products,
                    cartItems = cart,
                    onAddToCart = { cart.add(it) },
                    onCartClick = { navController.navigate(Screen.Cart.route) },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    cartItems = cart,
                    onBack = { navController.popBackStack() },
                    onCheckout = { navController.navigate(Screen.Checkout.route) },
                    onRemove = { product -> cart.remove(product) }
                )
            }

            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    cartItems = cart,
                    onPlaceOrder = {
                        cart.clear()
                        navController.popBackStack(Screen.ProductList.route, inclusive = false)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    email = loggedInEmail,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        cart.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
