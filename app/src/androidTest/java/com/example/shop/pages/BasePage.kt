package com.example.shop.pages

import com.example.shop.util.AppRule
import com.example.shop.util.HelperFunctions

open class BasePage(protected val rule: AppRule) {
    protected val hf = HelperFunctions(rule)
}
