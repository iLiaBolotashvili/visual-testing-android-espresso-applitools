package com.example.shop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
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
    val products = listOf(
        Product(1, "hm,mmmmmmm,mmmm", 999.0),
        Product(2, "ITEM", 199.0),
        Product(3, "123", 15.0),
        Product(4, "Laptop", 29.0)
    )
    val cart = remember { mutableStateListOf<Product>() }
    val loggedInEmail = "demo@shop.com" // or hoist from a state holder

    NavHost(navController, startDestination = Screen.Login.route) {
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
                        popUpTo(Screen.ProductList.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
