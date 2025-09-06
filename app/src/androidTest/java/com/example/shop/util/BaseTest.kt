package com.example.shop.util

import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.applitools.eyes.android.espresso.Eyes
import com.applitools.eyes.android.common.BatchInfo
import com.applitools.eyes.android.common.config.Configuration
import com.example.shop.MainActivity
import org.junit.*
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class BaseTest {

    companion object {
        private val sharedBatch = BatchInfo("Test Batch")
    }

    @get:Rule
    val testName = TestName()

    @get:Rule(order = 0)
    val composeRule = createAndroidComposeRule<MainActivity>()
    protected lateinit var eyes: Eyes

    @Before
    open fun setUp() {
        eyes = Eyes()

        eyes.apiKey = ""

        val deviceName = "${Build.MANUFACTURER}_${Build.MODEL}_API_${Build.VERSION.SDK_INT}"

        val config = Configuration().apply {
            appName = "Shop Demo"
            baselineEnvName = deviceName.replace(" ", "_")
            batch = sharedBatch
        }
        eyes.configuration = config

        eyes.open("Shop Demo", testName.methodName)
    }

    @After
    open fun tearDown() {
        try {
            eyes.close()
        } finally {
            eyes.abortIfNotClosed()
        }
    }
}
