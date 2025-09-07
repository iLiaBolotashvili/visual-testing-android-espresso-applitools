package com.example.shop.util

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*

class HelperFunctions(private val rule: AppRule) {

    fun waitForIdle() = rule.waitForIdle()

    fun waitUntilAppears(matcher: SemanticsMatcher, timeoutMs: Long = TestConfig.MEDIUM) {
        rule.waitUntil(timeoutMs) {
            rule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitForTag(tag: String, timeoutMs: Long = TestConfig.MEDIUM) =
        waitUntilAppears(hasTestTag(tag), timeoutMs)

    fun waitForText(text: String, substring: Boolean = false, timeoutMs: Long = TestConfig.MEDIUM) =
        waitUntilAppears(hasText(text, substring = substring, ignoreCase = true), timeoutMs)

    fun waitForContentDesc(desc: String, timeoutMs: Long = TestConfig.MEDIUM) =
        waitUntilAppears(hasContentDescription(desc, ignoreCase = true), timeoutMs)

    fun tapByTag(tag: String) {
        waitForTag(tag)
        rule.onNodeWithTag(tag).performClick()
    }

    fun tapByText(text: String, substring: Boolean = false) {
        val m = hasText(text, substring = substring, ignoreCase = true)
        waitUntilAppears(m)
        rule.onNode(m).performClick()
    }

    fun tapByContentDesc(desc: String) {
        val m = hasContentDescription(desc, ignoreCase = true)
        waitUntilAppears(m)
        rule.onNode(m).performClick()
    }

    fun scrollToTag(tag: String) {
        waitForTag(tag)
        rule.onNodeWithTag(tag).performScrollTo()
    }

    fun typeIntoTag(tag: String, text: String, clearFirst: Boolean = true, clickToFocus: Boolean = true) {
        waitForTag(tag)
        val node = rule.onNodeWithTag(tag)
        if (clickToFocus) node.performClick()
        if (clearFirst) node.performTextClearance()
        node.performTextInput(text)
    }

    fun assertTagExists(tag: String) {
        rule.onNodeWithTag(tag).assertExists()
    }

    fun assertTextExists(text: String, substring: Boolean = false) {
        rule.onNode(hasText(text, substring = substring, ignoreCase = true)).assertExists()
    }

    fun waitForLoginTransition() {
        try { waitForText("Signing in…", timeoutMs = TestConfig.SHORT) } catch (_: Throwable) {}
        waitForContentDesc("Cart", timeoutMs = TestConfig.LONG)
        waitForIdle()
    }


    fun enterTextInFieldWithLabel(
        labelText: String,
        text: String,
        clearFirst: Boolean = true
    ) {
        val base = hasSetTextAction()
        val exact = base and hasAnyDescendant(hasText(labelText, substring = false, ignoreCase = true))
        val substr = base and hasAnyDescendant(hasText(labelText, substring = true,  ignoreCase = true))

        val target = run {
            val exactFound = try {
                rule.onAllNodes(exact).fetchSemanticsNodes().isNotEmpty()
            } catch (_: Throwable) { false }
            if (exactFound) exact else substr
        }
        waitUntilAppears(target)

        val node = rule.onNode(target)
        try { node.performScrollTo() } catch (_: Throwable) {}

        node.performSemanticsAction(SemanticsActions.RequestFocus)

        if (clearFirst) node.performTextClearance()
        node.performTextInput(text)

        rule.waitForIdle()
    }

    fun waitAddButtonDisabled(tag: String, timeoutMs: Long = TestConfig.MEDIUM) {
        rule.waitUntil(timeoutMs) {
            try {
                rule.onNodeWithTag(tag).assertIsNotEnabled()
                true
            } catch (_: AssertionError) {
                false
            }
        }
    }

}
