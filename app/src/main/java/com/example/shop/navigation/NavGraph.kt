package com.example.shop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shop.data.Product
import com.example.shop.ui.screens.CartScreen
import com.example.shop.ui.screens.CheckoutScreen
import com.example.shop.ui.screens.LoginScreen
import com.example.shop.ui.screens.ProductListScreen
import com.example.shop.ui.screens.ProfileScreen

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
            Product(
                id = 1,
                name = "MacBook Pro 14”",
                brand = "Apple",
                price = 1999.0,
                imageUrl = "https://picsum.photos/seed/laptop1/640/480",
                description = "M3 performance with Liquid Retina XDR. Great for dev and creative workloads."
            ),
            Product(
                id = 2,
                name = "Noise Cancelling Headphones",
                brand = "Bose",
                price = 299.0,
                imageUrl = "https://picsum.photos/seed/headphones1/640/480",
                description = "Comfortable over-ear design with world-class ANC for travel and focus."
            ),
            Product(
                id = 3,
                name = "USB-C Charger 67W",
                brand = "Anker",
                price = 39.99,
                imageUrl = "https://picsum.photos/seed/charger1/640/480",
                description = "Compact GaN fast charger for laptops, tablets, and phones."
            ),
            Product(
                id = 4,
                name = "Mechanical Keyboard TKL",
                brand = "Keychron",
                price = 129.0,
                imageUrl = "https://picsum.photos/seed/keyboard1/640/480",
                description = "Hot-swappable switches, RGB backlight, and durable PBT keycaps."
            ),
            Product(
                id = 5,
                name = "Galaxy S24 Ultra",
                brand = "Samsung",
                price = 1199.0,
                imageUrl = "https://picsum.photos/seed/phone1/640/480",
                description = "Flagship smartphone with incredible camera and bright AMOLED display."
            ),
            Product(
                id = 6,
                name = "iPad Air 11”",
                brand = "Apple",
                price = 699.0,
                imageUrl = "https://picsum.photos/seed/tablet1/640/480",
                description = "Lightweight tablet with M2 chip and Apple Pencil support."
            ),
            Product(
                id = 7,
                name = "Smartwatch GT4",
                brand = "Huawei",
                price = 249.0,
                imageUrl = "https://picsum.photos/seed/watch1/640/480",
                description = "Stylish smartwatch with health tracking and 2-week battery life."
            ),
            Product(
                id = 8,
                name = "Surface Laptop Go 3",
                brand = "Microsoft",
                price = 899.0,
                imageUrl = "https://picsum.photos/seed/laptop2/640/480",
                description = "Slim, lightweight laptop perfect for students and everyday use."
            ),
            Product(
                id = 9,
                name = "AirPods Pro 2",
                brand = "Apple",
                price = 249.0,
                imageUrl = "https://picsum.photos/seed/earbuds1/640/480",
                description = "Wireless earbuds with adaptive noise cancellation and spatial audio."
            ),
            Product(
                id = 10,
                name = "PlayStation 5",
                brand = "Sony",
                price = 499.0,
                imageUrl = "https://picsum.photos/seed/console1/640/480",
                description = "Next-gen gaming console with stunning graphics and exclusive titles."
            ),
            Product(
                id = 11,
                name = "4K UltraWide Monitor",
                brand = "LG",
                price = 699.0,
                imageUrl = "https://picsum.photos/seed/monitor1/640/480",
                description = "34-inch ultrawide monitor with HDR10 and razor-thin bezels."
            ),
            Product(
                id = 12,
                name = "Portable Bluetooth Speaker",
                brand = "JBL",
                price = 149.0,
                imageUrl = "https://picsum.photos/seed/speaker1/640/480",
                description = "Rugged, waterproof speaker with deep bass and 12-hour battery life."
            )
        )
    }

    val cart = remember { mutableStateListOf<Product>() }

    val loggedInEmail = "demo@shop.com"

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
                }
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
                onCheckout = { navController.navigate(Screen.Checkout.route) }
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
