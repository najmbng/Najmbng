package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.OsmoCyanGlow
import com.example.ui.theme.OsmoTeal
import com.example.ui.theme.ReelCrimson
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleContainer
import com.example.ui.theme.SleekPurpleOnContainer
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekSurfaceVariant
import com.example.ui.theme.SleekTextMuted
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Reframe360Viewer(
    initialYaw: Float = 0f,
    initialPitch: Float = -5f,
    initialFov: Float = 110f,
    framingLabel: String = "Selfie POV",
    modifier: Modifier = Modifier,
    aspectRatioMode: String = "9:16", // "9:16" or "16:9"
    onAngleChanged: (yaw: Float, pitch: Float, fov: Float) -> Unit = { _, _, _ -> }
) {
    var yaw by remember(initialYaw) { mutableFloatStateOf(initialYaw) }
    var pitch by remember(initialPitch) { mutableFloatStateOf(initialPitch) }
    var fov by remember(initialFov) { mutableFloatStateOf(initialFov) }
    var isTinyPlanet by remember { mutableStateOf(framingLabel.contains("Planet", ignoreCase = true)) }

    val animatedYaw by animateFloatAsState(targetValue = yaw, label = "yawAnim")
    val animatedPitch by animateFloatAsState(targetValue = pitch, label = "pitchAnim")

    CardFrame(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with telemetry
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(SleekPurple, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "360° REFRAME VIEWPORT",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                }

                Surface(
                    color = SleekPurpleContainer,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekPurple.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = framingLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekPurpleOnContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 360 Simulated Canvas with Drag Controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                    .testTag("viewport_360_canvas")
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val newYaw = (yaw + dragAmount.x * 0.4f).coerceIn(-180f, 180f)
                            val newPitch = (pitch - dragAmount.y * 0.3f).coerceIn(-85f, 85f)
                            yaw = newYaw
                            pitch = newPitch
                            onAngleChanged(yaw, pitch, fov)
                        }
                    }
            ) {
                // Interactive 360 Equirectangular & Reframe Simulation Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Equirectangular Grid & Atmosphere
                    val horizonY = h * 0.5f + (animatedPitch * 1.2f)
                    
                    // Sky / Mountain / Ground representation
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF818CF8)),
                            startY = 0f,
                            endY = horizonY
                        ),
                        size = Size(w, horizonY)
                    )
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155)),
                            startY = horizonY,
                            endY = h
                        ),
                        topLeft = Offset(0f, horizonY),
                        size = Size(w, h - horizonY)
                    )

                    // Equirectangular longitude grid lines moving with yaw
                    val yawOffset = (animatedYaw / 180f) * (w * 0.5f)
                    for (i in -4..4) {
                        val lineX = (w * 0.5f) + yawOffset + (i * w * 0.25f)
                        if (lineX in 0f..w) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.2f),
                                start = Offset(lineX, 0f),
                                end = Offset(lineX, h),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }

                    // Simulated 360 Subject / Invisible Stick Point
                    val subjectYawRad = Math.toRadians((animatedYaw).toDouble())
                    val subjectX = (w * 0.5f) + (sin(subjectYawRad) * (w * 0.35f)).toFloat()
                    val subjectY = horizonY + 15.dp.toPx()

                    if (!isTinyPlanet) {
                        // Draw simulated subject / selfie action icon
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF818CF8), Color.Transparent),
                                radius = 24.dp.toPx()
                            ),
                            radius = 20.dp.toPx(),
                            center = Offset(subjectX, subjectY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = Offset(subjectX, subjectY)
                        )
                    } else {
                        // Tiny Planet Curvature
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFF312E81), Color.Transparent),
                                center = Offset(w * 0.5f, h * 0.6f),
                                radius = 60.dp.toPx()
                            ),
                            radius = 55.dp.toPx(),
                            center = Offset(w * 0.5f, h * 0.6f)
                        )
                    }

                    // 9:16 or 16:9 Safe Crop Framing Box overlay
                    val cropW = if (aspectRatioMode == "9:16") (h * (9f / 16f) * 0.8f) else (w * 0.7f)
                    val cropH = if (aspectRatioMode == "9:16") (h * 0.8f) else (w * 0.7f * (9f / 16f))
                    val cropLeft = (w - cropW) / 2f
                    val cropTop = (h - cropH) / 2f

                    drawRect(
                        color = if (aspectRatioMode == "9:16") Color(0xFFEF4444) else Color(0xFFF59E0B),
                        topLeft = Offset(cropLeft, cropTop),
                        size = Size(cropW, cropH),
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // Center Crosshair
                    val centerX = w * 0.5f
                    val centerY = h * 0.5f
                    drawLine(
                        color = Color(0xFFA5B4FC),
                        start = Offset(centerX - 10.dp.toPx(), centerY),
                        end = Offset(centerX + 10.dp.toPx(), centerY),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFFA5B4FC),
                        start = Offset(centerX, centerY - 10.dp.toPx()),
                        end = Offset(centerX, centerY + 10.dp.toPx()),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }

                // Interactive Overlay telemetry tag
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "DRAG TO REFRAME 360°",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Angle Telemetry Readout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryPill(label = "YAW", value = String.format(Locale.ROOT, "%+.1f°", yaw), color = SleekPurple)
                TelemetryPill(label = "PITCH", value = String.format(Locale.ROOT, "%+.1f°", pitch), color = Color(0xFFD97706))
                TelemetryPill(label = "FOV", value = String.format(Locale.ROOT, "%.0f°", fov), color = Color(0xFF0284C7))
                TelemetryPill(label = "CROP", value = aspectRatioMode, color = if (aspectRatioMode == "9:16") Color(0xFFDC2626) else Color(0xFFD97706))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Framing Presets
            Text(
                text = "Preset Reframes:",
                style = MaterialTheme.typography.labelSmall,
                color = SleekTextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetChip(
                    title = "Selfie (0°)",
                    isSelected = yaw in -15f..15f && !isTinyPlanet,
                    onClick = {
                        yaw = 0f
                        pitch = -5f
                        fov = 115f
                        isTinyPlanet = false
                        onAngleChanged(yaw, pitch, fov)
                    }
                )
                PresetChip(
                    title = "Behind (+180°)",
                    isSelected = yaw > 160f || yaw < -160f,
                    onClick = {
                        yaw = 180f
                        pitch = 10f
                        fov = 110f
                        isTinyPlanet = false
                        onAngleChanged(yaw, pitch, fov)
                    }
                )
                PresetChip(
                    title = "Tiny Planet",
                    isSelected = isTinyPlanet,
                    onClick = {
                        yaw = 0f
                        pitch = -75f
                        fov = 145f
                        isTinyPlanet = true
                        onAngleChanged(yaw, pitch, fov)
                    }
                )
            }
        }
    }
}

@Composable
fun TelemetryPill(label: String, value: String, color: Color) {
    Surface(
        color = SleekSurfaceVariant,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SleekTextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun PresetChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) SleekPurpleContainer else SleekSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) SleekPurple else SleekBorderSubtle
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) SleekPurpleOnContainer else SleekTextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

