package com.example.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.shop.navigation.ShopNavGraph
import com.example.shop.ui.theme.ShopTheme
import com.example.shop.ui.widgets.AppBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopTheme {
                AppBackground {
                    val navController = rememberNavController()
                    ShopNavGraph(navController = navController)
                }
            }
        }
    }
}
