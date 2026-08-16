package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EdlItemEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.CardFrame
import com.example.ui.components.EdlTimelineStrip
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.DaVinciPurple
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
fun EdlStudioScreen(
    project: ProjectEntity?,
    edlItems: List<EdlItemEntity>,
    isAnalyzing: Boolean,
    onRegenerateEdlClick: () -> Unit,
    onExportDaVinciClick: () -> Unit
) {
    val totalSeconds = edlItems.sumOf { it.durationSeconds.toDouble() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("edl_studio_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // EDL Header & Export Action Card
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
                        Column {
                            Text(
                                text = "DaVinci Resolve Rough Cut (EDL)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            Text(
                                text = "Pacing: ${project?.preferredPacing ?: "Dynamic Social"} • ${project?.targetFramerate ?: 30} FPS",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekTextSecondary
                            )
                        }

                        Surface(
                            color = SleekPurpleContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "${edlItems.size} CUTS • ${String.format("%.1fs", totalSeconds)}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SleekPurpleOnContainer,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Export & Regenerate buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onExportDaVinciClick,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("export_davinci_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FileDownload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Export for DaVinci",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        FilledTonalButton(
                            onClick = onRegenerateEdlClick,
                            modifier = Modifier.testTag("regenerate_edl_button"),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = SleekPurpleContainer,
                                contentColor = SleekPurpleOnContainer
                            ),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isAnalyzing
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = SleekPurple
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Rebuild EDL", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Visual Timeline Strip
        item {
            EdlTimelineStrip(edlItems = edlItems)
        }

        // Cut List Header
        item {
            Text(
                text = "Edit Decision Sequence Breakdown",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary
            )
        }

        // Cut List Items
        if (edlItems.isEmpty()) {
            item {
                CardFrame(
                    backgroundColor = SleekSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No cuts generated yet. Tap 'Rebuild EDL' to generate.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SleekTextSecondary
                        )
                    }
                }
            }
        } else {
            items(edlItems, key = { it.id }) { item ->
                EdlItemRowCard(item = item)
            }
        }
    }
}

@Composable
fun EdlItemRowCard(item: EdlItemEntity) {
    val markerColor = when (item.resolveMarkerColor.lowercase()) {
        "cyan" -> SleekPurple
        "green" -> LandscapeEmerald
        "orange" -> CinemaGold
        "magenta" -> ReelCrimson
        else -> DaVinciPurple
    }

    Surface(
        color = SleekSurface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("edl_row_${item.sequenceOrder}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Cut # and clip name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(markerColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.sequenceOrder}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = markerColor
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.clipName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary
                        )
                        Text(
                            text = "Transition: ${item.transition} • Duration: ${item.durationSeconds}s",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekTextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    color = SleekSurfaceVariant,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.speedRamp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = CinemaGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timecode Strip
            Surface(
                color = SleekSurfaceVariant,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "SOURCE (IN ➔ OUT)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekTextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${item.sourceInTimecode} ➔ ${item.sourceOutTimecode}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SleekPurple,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "TIMELINE REC (IN ➔ OUT)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekTextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${item.recordInTimecode} ➔ ${item.recordOutTimecode}",
                            style = MaterialTheme.typography.bodySmall,
                            color = LandscapeEmerald,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 360 Reframe Directive
            Text(
                text = "🎥 360 Reframe: ${item.cameraReframeDirective}",
                style = MaterialTheme.typography.bodySmall,
                color = SleekTextPrimary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Color Grade Note
            Text(
                text = "🎨 Grade: ${item.gradingNote}",
                style = MaterialTheme.typography.labelSmall,
                color = SleekTextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

