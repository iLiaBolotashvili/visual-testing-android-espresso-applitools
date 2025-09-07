package com.example.shop.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Subtle static background with gradient + soft blobs.
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val baseTop = Color(0xFFF9F9F9)
    val baseBottom = Color(0xFFFFFFFF)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(baseTop, baseBottom)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val radius = maxOf(w, h) * 0.35f

            val p1 = Offset(w * 0.25f, h * 0.3f)
            val p2 = Offset(w * 0.75f, h * 0.25f)
            val p3 = Offset(w * 0.5f, h * 0.8f)

            drawSoftBlob(center = p1, radius = radius, color = Color(0xFF7C93FF), alpha = 0.06f)
            drawSoftBlob(center = p2, radius = radius * 0.9f, color = Color(0xFF6EE7B7), alpha = 0.05f)
            drawSoftBlob(center = p3, radius = radius * 1.1f, color = Color(0xFFFFD166), alpha = 0.05f)
        }

        content()
    }
}

private fun DrawScope.drawSoftBlob(center: Offset, radius: Float, color: Color, alpha: Float) {
    drawCircle(color = color.copy(alpha = alpha), radius = radius, center = center)
    drawCircle(color = color.copy(alpha = alpha * 0.6f), radius = radius * 0.6f, center = center)
}
