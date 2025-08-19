package com.example.shop.tests

import com.example.shop.util.BaseTest
import org.junit.Test

class VisualTests : BaseTest() {
    @Test
    fun visualTestExample() {
        eyes.checkWindow("Main Activity")
    }
}