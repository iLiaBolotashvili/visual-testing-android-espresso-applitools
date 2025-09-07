package com.example.shop.util

import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasTestTag
import com.applitools.eyes.android.common.BatchInfo
import com.applitools.eyes.android.common.config.Configuration
import com.applitools.eyes.android.espresso.Eyes
import com.example.shop.MainActivity
import org.junit.*
import org.junit.rules.TestName
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import android.os.SystemClock
import android.util.Log
import androidx.compose.ui.test.hasText
import com.applitools.eyes.android.espresso.fluent.Target

@RunWith(AndroidJUnit4::class)
open class BaseTest {

    companion object {
        private val sharedBatch = BatchInfo("my first test batch")
    }

    @get:Rule
    val testName = TestName()

    @get:Rule(order = 0)
    val composeRule = createAndroidComposeRule<MainActivity>()

    protected lateinit var eyes: Eyes

    @Before
    open fun setUp() {
        eyes = Eyes().apply {
            apiKey = "YOUR_API_KEY"
            configuration = Configuration().apply {
                appName = "Shop Demo"
                baselineEnvName = "${Build.MANUFACTURER}_${Build.MODEL}_API_${Build.VERSION.SDK_INT}".replace(" ", "_")
                batch = sharedBatch
            }
        }
        eyes.open("Shop Demo", testName.methodName)

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodes(hasTestTag("email_field")).fetchSemanticsNodes().isNotEmpty()
        }
    }

    protected fun snapshot(
        tag: String,
        waitForText: String? = null,
        substring: Boolean = false,
        settleMs: Long = 400
    ) {
        waitForText?.let { text ->
            val m = hasText(text, substring = substring, ignoreCase = true)
            composeRule.waitUntil(5_000) {
                composeRule.onAllNodes(m).fetchSemanticsNodes().isNotEmpty()
            }
        }
        composeRule.waitForIdle()
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        if (settleMs > 0) SystemClock.sleep(settleMs)

        eyes.check(Target.window().withName(tag))
    }

    @After
    open fun tearDown() {
        try {
            eyes.close()
        } catch (e: com.applitools.eyes.android.core.DiffsFoundException) {
            Log.w("Eyes", "Diffs found: ${e.message}")
        } finally {
            eyes.abortIfNotClosed()
        }
    }
}
