package ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import ui.theme.LocalDimens
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Loading(large: Boolean = true) {
    val dimens = LocalDimens.current
    val circleCount = 4

    val colors = listOf(
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.secondary
    )

    val baseSize = if (large) dimens.largePadding else dimens.mediumPadding
    val spacing = if (large) dimens.mediumPadding else dimens.smallPadding
    val padding = if (large) dimens.largePadding else dimens.mediumPadding

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier.padding(padding)
    ) {
        repeat(circleCount) { index ->
            SmoothBouncingCircle(
                baseSize = baseSize,
                color = colors[index % colors.size],
                index = index,
                large = large
            )
        }
    }
}

@Composable
private fun SmoothBouncingCircle1(baseSize: Dp, color: Color, index: Int, large: Boolean) {
    val dimens = LocalDimens.current
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
                delayMillis = index * 150
            )
        )
    )

    val verticalRange = if (large) dimens.largePadding else dimens.smallPadding
    val offsetY = with(LocalDensity.current) {
        // Linear vertical movement without bounce curve
        val yProgress = if (animationProgress < 0.5f) {
            animationProgress * 2  // 0 -> 1 (up)
        } else {
            2 - animationProgress * 2  // 1 -> 0 (down)
        }
        (yProgress * verticalRange.toPx() * 2).toDp()
    }

    Box(
        modifier = Modifier
            .size(baseSize)
            .graphicsLayer {
                translationY = -offsetY.toPx()
            }
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
private fun SmoothBouncingCircle(baseSize: Dp, color: Color, index: Int, large: Boolean) {
    val dimens = LocalDimens.current
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                // Increased duration for smoother animation
                durationMillis = 2000,
                easing = LinearEasing,
                // Increased delay between circles for better visual effect
                delayMillis = index * 200
            )
        )
    )

    // Reduced vertical range for less dramatic movement
    val verticalRange = if (large) dimens.mediumPadding else dimens.smallPadding
    val offsetY = with(LocalDensity.current) {
        // Reduced frequency (3.0 instead of 4.5) for smoother, slower oscillation
        (sin((animationProgress * 3.0 * Math.PI).toFloat()) * verticalRange.toPx()).toDp()
    }

    // Reduced scale range for subtler scaling
    val minScale = if (large) 0.85f else 0.9f
    val maxScale = if (large) 1.15f else 1.1f
    // Reduced frequency (2.0 instead of 2.5) for smoother scaling
    val scale = minScale + (maxScale - minScale) * abs(cos(animationProgress * 2.0 * Math.PI).toFloat())

    Box(
        modifier = Modifier
            .size(baseSize)
            .graphicsLayer {
                translationY = -offsetY.toPx()
//                scaleX = scale
//                scaleY = scale
            }
            .clip(CircleShape)
            .background(color)
            .graphicsLayer {
                // Subtle alpha changes for a more refined look
//                alpha = 0.85f + (scale - minScale) * 0.15f
            }
    )
}

@Composable
private fun WaveCircle(baseSize: Dp, color: Color, index: Int, large: Boolean) {
    val dimens = LocalDimens.current
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing,
                delayMillis = index * 200
            )
        )
    )

    // Calculate horizontal position for wave effect
    val horizontalOffset = with(LocalDensity.current) {
        // Create a wave pattern with sin function
        (sin((animationProgress * 2.0 * Math.PI).toFloat()) * dimens.noPadding.toPx()).toDp()
    }

    // Alpha animation for wave effect
    val alpha = 0.5f + 0.5f * sin((animationProgress * 2.0 * Math.PI).toFloat())

    Box(
        modifier = Modifier
            .size(baseSize)
            .graphicsLayer {
                // No vertical translation (removed bounce)
                translationX = horizontalOffset.toPx() // Optional: adds subtle horizontal movement
                // No scaling (removed bounce scaling)
                this.alpha = alpha // Animate opacity for wave effect
            }
            .clip(CircleShape)
            .background(color)
    )
}
