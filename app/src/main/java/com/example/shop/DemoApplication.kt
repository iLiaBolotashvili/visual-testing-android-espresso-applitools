package com.example.shop

import android.app.Application
import android.content.pm.ApplicationInfo
import coil.ImageLoader
import coil.ImageLoaderFactory

class DemoApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return ImageLoader.Builder(this)
            .allowHardware(!isDebuggable)
            .build()
    }
}
