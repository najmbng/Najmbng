package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClipEntity
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
fun ClipRankCard(
    clip: ClipEntity,
    modifier: Modifier = Modifier,
    onInspectClick: (ClipEntity) -> Unit,
    onAnalyzeClick: (ClipEntity) -> Unit
) {
    val isReelsFav = clip.reelsScore >= clip.landscapeScore

    CardFrame(
        modifier = modifier.testTag("clip_card_${clip.id}"),
        backgroundColor = SleekSurface,
        borderColor = SleekBorder,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Title, Resolution badge, Platform winner badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(SleekPurpleContainer, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = SleekPurpleOnContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = clip.fileName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${clip.resolution} • ${clip.fps} FPS",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekTextSecondary,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "⏱ ${String.format("%.1fs", clip.durationSeconds)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CinemaGold,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Platform Winner Badge
                Surface(
                    color = if (isReelsFav) ReelCrimson.copy(alpha = 0.1f) else LandscapeEmerald.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isReelsFav) ReelCrimson.copy(alpha = 0.4f) else LandscapeEmerald.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isReelsFav) Icons.Default.CropPortrait else Icons.Default.Landscape,
                            contentDescription = null,
                            tint = if (isReelsFav) ReelCrimson else LandscapeEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isReelsFav) "🔥 Best for Reels" else "🌄 Best for 16:9",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isReelsFav) ReelCrimson else LandscapeEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scene Summary
            if (clip.sceneDescription.isNotEmpty()) {
                Text(
                    text = clip.sceneDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = SleekTextSecondary,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Dual Score Ranking Bars (Reels vs Landscape)
            Surface(
                color = SleekSurfaceVariant,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Reels Score Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CropPortrait,
                                contentDescription = null,
                                tint = ReelCrimson,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Reels / Shorts (9:16)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${clip.reelsScore}/100",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ReelCrimson,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    LinearProgressIndicator(
                        progress = { clip.reelsScore / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ReelCrimson,
                        trackColor = Color(0xFFFFE4E6)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Landscape Score Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Landscape,
                                contentDescription = null,
                                tint = LandscapeEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Landscape / Community (16:9)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${clip.landscapeScore}/100",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = LandscapeEmerald,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    LinearProgressIndicator(
                        progress = { clip.landscapeScore / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = LandscapeEmerald,
                        trackColor = Color(0xFFE0F2FE)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Inspect 360 & Analyze
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = { onInspectClick(clip) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("inspect_clip_button_${clip.id}"),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = SleekPurpleContainer,
                        contentColor = SleekPurpleOnContainer
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Inspect 360 Angles",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onAnalyzeClick(clip) },
                    modifier = Modifier.testTag("ai_reanalyze_button_${clip.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CinemaGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI 360",
                        style = MaterialTheme.typography.labelMedium,
                        color = CinemaGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

