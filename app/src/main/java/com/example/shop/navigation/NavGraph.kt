package com.example.shop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shop.data.Product
import com.example.shop.ui.screens.CartScreen
import com.example.shop.ui.screens.CheckoutScreen
import com.example.shop.ui.screens.ProductListScreen

sealed class Screen(val route: String) {
    data object ProductList : Screen("product_list")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
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

    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {
        composable(Screen.ProductList.route) {
            ProductListScreen(
                products = products,
                onAddToCart = { cart.add(it) },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                cartCount = cart.size
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
    }
}
