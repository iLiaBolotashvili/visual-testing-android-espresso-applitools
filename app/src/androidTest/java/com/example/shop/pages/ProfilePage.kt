package com.example.shop.pages

import com.example.shop.util.AppRule

class ProfilePage(rule: AppRule) : BasePage(rule) {

    fun assertEmailShown(email: String): ProfilePage {
        hf.assertTextExists(email)
        return this
    }

    fun logout(): LoginPage {
        hf.tapByText("Log out")
        hf.waitForTag("email_field")
        return LoginPage(rule)
    }
}
