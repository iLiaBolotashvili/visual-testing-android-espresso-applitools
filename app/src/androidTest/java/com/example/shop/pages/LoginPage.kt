package com.example.shop.pages

import androidx.compose.ui.test.hasText
import com.example.shop.util.AppRule

class LoginPage(rule: AppRule) : BasePage(rule) {
    private val emailTag = "email_field"
    private val passwordTag = "password_field"
    private val signInTag = "sign_in_button"
    private val toggleGlitchTag = "toggle_glitch_fab"
    private val errorTag = "error_text"

    fun toggleBugs(on: Boolean): LoginPage {
        val isOn = rule.onAllNodes(hasText("Visual Bugs: ON", substring = true))
            .fetchSemanticsNodes().isNotEmpty()
        if (on != isOn) hf.tapByTag(toggleGlitchTag)
        return this
    }

    fun enterEmail(email: String): LoginPage { hf.typeIntoTag(emailTag, email); return this }
    fun enterPassword(password: String): LoginPage { hf.typeIntoTag(passwordTag, password); return this }

    fun submit(): ProductsListPage {
        hf.tapByTag(signInTag)
        hf.waitForLoginTransition()
        return ProductsListPage(rule)
    }

    fun assertErrorShown(): LoginPage { hf.assertTagExists(errorTag); return this }
}
