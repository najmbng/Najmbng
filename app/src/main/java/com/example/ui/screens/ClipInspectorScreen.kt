package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClipEntity
import com.example.data.model.KeyframeAngleEntity
import com.example.ui.components.CardFrame
import com.example.ui.components.Reframe360Viewer
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.LandscapeEmerald
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

@Composable
fun ClipInspectorScreen(
    clip: ClipEntity?,
    keyframes: List<KeyframeAngleEntity>,
    selectedKeyframe: KeyframeAngleEntity?,
    isAnalyzing: Boolean,
    onKeyframeSelect: (KeyframeAngleEntity) -> Unit,
    onAnalyzeClipClick: (ClipEntity) -> Unit
) {
    if (clip == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a clip from the Batch Ranking tab to inspect 360 angles.",
                style = MaterialTheme.typography.bodyLarge,
                color = SleekTextSecondary
            )
        }
        return
    }

    var aspectRatioMode by remember { mutableStateOf("9:16") }
    val activeKf = selectedKeyframe ?: keyframes.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("clip_inspector_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Clip Header Card
        item {
            CardFrame(
                backgroundColor = SleekSurface,
                borderColor = SleekBorder,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = clip.fileName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${clip.resolution} • ${clip.fps} FPS • Duration: ${String.format("%.1fs", clip.durationSeconds)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SleekPurple,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { onAnalyzeClipClick(clip) },
                        modifier = Modifier.testTag("inspector_reanalyze_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPurple,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isAnalyzing
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Re-Analyze", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 360 Simulated Reframe Viewport
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Interactive Framing Viewport",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextSecondary
                    )

                    // Crop Toggle Mode (9:16 vs 16:9)
                    Row(
                        modifier = Modifier
                            .background(SleekSurface, RoundedCornerShape(10.dp))
                            .border(1.dp, SleekBorder, RoundedCornerShape(10.dp))
                            .padding(2.dp)
                    ) {
                        Surface(
                            onClick = { aspectRatioMode = "9:16" },
                            color = if (aspectRatioMode == "9:16") ReelCrimson.copy(alpha = 0.15f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CropPortrait,
                                    contentDescription = null,
                                    tint = if (aspectRatioMode == "9:16") ReelCrimson else SleekTextSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "9:16 Reels",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (aspectRatioMode == "9:16") ReelCrimson else SleekTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            onClick = { aspectRatioMode = "16:9" },
                            color = if (aspectRatioMode == "16:9") LandscapeEmerald.copy(alpha = 0.15f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Landscape,
                                    contentDescription = null,
                                    tint = if (aspectRatioMode == "16:9") LandscapeEmerald else SleekTextSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "16:9",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (aspectRatioMode == "16:9") LandscapeEmerald else SleekTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Reframe360Viewer(
                    initialYaw = activeKf?.yawDegrees ?: 0f,
                    initialPitch = activeKf?.pitchDegrees ?: -5f,
                    initialFov = activeKf?.fovDegrees ?: 110f,
                    framingLabel = activeKf?.framingType ?: "Dynamic 360",
                    aspectRatioMode = aspectRatioMode
                )
            }
        }

        // Active Keyframe Deep Dive Card
        if (activeKf != null) {
            item {
                CardFrame(
                    backgroundColor = SleekSurface,
                    borderColor = SleekBorder,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = CinemaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activeKf.momentTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }

                            Surface(
                                color = SleekPurpleContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Score: ${activeKf.qualityScore}/100",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SleekPurpleOnContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reasoning Box
                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🎯 AI MOMENT REASONING",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekPurple,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeKf.reasoning,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SleekTextPrimary,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Recommended Reframe Directive
                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🎬 DAVINCI REFRAME DIRECTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CinemaGold,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeKf.recommendedReframe,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SleekTextPrimary,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Detected Moments / Keyframes List
        item {
            Text(
                text = "Detected 360 Moments & Angles (${keyframes.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary
            )
        }

        items(keyframes, key = { it.id }) { kf ->
            val isSelected = kf.id == activeKf?.id
            KeyframeListItem(
                keyframe = kf,
                isSelected = isSelected,
                onClick = { onKeyframeSelect(kf) }
            )
        }
    }
}

@Composable
fun KeyframeListItem(
    keyframe: KeyframeAngleEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) SleekPurpleContainer else SleekSurface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) SleekPurple else SleekBorder
        ),
        shadowElevation = if (isSelected) 2.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("keyframe_item_${keyframe.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isSelected) SleekPurple else SleekSurfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${String.format("%.1f", keyframe.timestampSeconds)}s",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else SleekPurple,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = keyframe.momentTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) SleekPurpleOnContainer else SleekTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${keyframe.timecodeIn} ➔ ${keyframe.timecodeOut} | Yaw: ${String.format("%+.0f°", keyframe.yawDegrees)} Pitch: ${String.format("%+.0f°", keyframe.pitchDegrees)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) SleekPurpleOnContainer.copy(alpha = 0.8f) else SleekTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }
            }

            Surface(
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else SleekSurfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = keyframe.framingType,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) SleekPurple else SleekTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

