package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun WaveformVisualizer(
    isSpeaking: Boolean,
    amplitude: Float,
    pitch: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_phase")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase_anim"
    )

    val animatedAmp = remember { Animatable(0f) }
    LaunchedEffect(isSpeaking, amplitude) {
        if (isSpeaking) {
            animatedAmp.animateTo(
                targetValue = amplitude.coerceAtLeast(0.25f),
                animationSpec = tween(80)
            )
        } else {
            animatedAmp.animateTo(0.05f, animationSpec = tween(300))
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(surfaceColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("waveform_visualizer"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            val barCount = 36
            val barSpacing = size.width / barCount
            val barWidth = (barSpacing * 0.6f).coerceIn(3f, 10f)
            val centerY = size.height / 2f
            val maxBarHeight = size.height * 0.85f

            for (i in 0 until barCount) {
                val progress = i.toFloat() / barCount
                val waveOffset = (i * 0.45f) * (pitch.coerceIn(0.5f, 2.0f))
                val waveFactor = if (isSpeaking) {
                    kotlin.math.abs(kotlin.math.sin(phase + waveOffset)).toFloat()
                } else {
                    0.15f + 0.1f * kotlin.math.sin(i * 0.3).toFloat()
                }

                val currentAmp = animatedAmp.value
                val barHeight = (maxBarHeight * (waveFactor * currentAmp + 0.08f)).coerceIn(4f, maxBarHeight)

                val x = i * barSpacing + (barSpacing - barWidth) / 2f
                val y = centerY - (barHeight / 2f)

                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        secondaryColor,
                        primaryColor,
                        Color(0xFF00CEC9)
                    ),
                    startY = y,
                    endY = y + barHeight
                )

                drawRoundRect(
                    brush = gradient,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                )
            }
        }
    }
}
