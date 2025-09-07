package com.example.shop.pages

import com.example.shop.util.AppRule

class CheckoutPage(rule: AppRule) : BasePage(rule) {

    fun fillShippingAndPayment(
        fullName: String,
        address: String,
        email: String,
        card: String
    ): CheckoutPage {
        hf.enterTextInFieldWithLabel("Full name", fullName)
        hf.enterTextInFieldWithLabel("Address", address)
        hf.enterTextInFieldWithLabel("Email", email)
        hf.enterTextInFieldWithLabel("Card (**** **** **** 1234)", card)
        return this
    }

    fun placeOrder(): ProductsListPage {
        hf.tapByText("Place order")
        hf.waitForText("Shop")
        return ProductsListPage(rule)
    }
}
