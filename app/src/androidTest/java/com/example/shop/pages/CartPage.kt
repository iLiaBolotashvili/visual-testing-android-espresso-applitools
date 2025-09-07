package com.example.shop.pages

import com.example.shop.util.AppRule

class CartPage(rule: AppRule) : BasePage(rule) {

    fun removeFirstItem(): CartPage {
        hf.tapByContentDesc("Remove item")
        return this
    }

    fun checkout(): CheckoutPage {
        hf.tapByText("Checkout")
        hf.waitForText("Shipping & Payment")
        return CheckoutPage(rule)
    }

    fun assertEmptyState(): CartPage {
        hf.assertTextExists("Your cart is empty")
        return this
    }
}
