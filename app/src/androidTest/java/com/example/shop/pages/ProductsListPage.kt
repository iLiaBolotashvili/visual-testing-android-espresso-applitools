package com.example.shop.pages

import com.example.shop.util.AppRule

class ProductsListPage(rule: AppRule) : BasePage(rule) {

    fun addProductById(id: Int): ProductsListPage {
        val tag = "add_$id"
        hf.scrollToTag(tag)
        hf.tapByTag(tag)
        hf.waitAddButtonDisabled(tag)
        return this
    }

    fun openCart(): CartPage {
        hf.tapByContentDesc("Cart")
        hf.waitForText("Your Cart")
        return CartPage(rule)
    }

    fun openProfile(): ProfilePage {
        hf.tapByText("P")
        hf.waitForText("Profile")
        return ProfilePage(rule)
    }
}
