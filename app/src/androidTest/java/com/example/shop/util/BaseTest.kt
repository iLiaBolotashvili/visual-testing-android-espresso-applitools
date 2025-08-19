package com.example.shop.util

import androidx.test.ext.junit.rules.ActivityScenarioRule
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
        private val sharedBatch = BatchInfo("Workshop Batch")
    }

    @get:Rule
    val testName = TestName()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    protected lateinit var eyes: Eyes

    @Before
    open fun setUp() {
        eyes = Eyes()

        eyes.apiKey = "XHZb3cDOhIBPMlCPe4P101igz1Wc108of2cptDG0moJfV3c110"

        val config = Configuration().apply {
            appName = "Shop Demo"
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
