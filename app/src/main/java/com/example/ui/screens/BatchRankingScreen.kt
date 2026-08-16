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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClipEntity
import com.example.ui.components.CardFrame
import com.example.ui.components.ClipRankCard
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.LandscapeEmerald
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
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurpleOnContainer
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekSurfaceVariant
import com.example.ui.theme.SleekTextMuted
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import com.example.ui.viewmodel.PlatformFilter

@Composable
fun BatchRankingScreen(
    clips: List<ClipEntity>,
    platformFilter: PlatformFilter,
    isAnalyzing: Boolean,
    onFilterChange: (PlatformFilter) -> Unit,
    onBatchAnalyzeClick: () -> Unit,
    onAddClipClick: () -> Unit,
    onInspectClipClick: (ClipEntity) -> Unit,
    onAnalyzeClipClick: (ClipEntity) -> Unit
) {
    val filteredClips = when (platformFilter) {
        PlatformFilter.ALL -> clips
        PlatformFilter.REELS_FIRST -> clips.sortedByDescending { it.reelsScore }
        PlatformFilter.LANDSCAPE_FIRST -> clips.sortedByDescending { it.landscapeScore }
    }

    val reelsCount = clips.count { it.reelsScore >= it.landscapeScore }
    val landscapeCount = clips.size - reelsCount

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("batch_ranking_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Batch Action Card in Sleek Interface Lavender Style
        item {
            CardFrame(
                backgroundColor = SleekPurpleContainer,
                borderColor = Color.Transparent,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 0.dp,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.8f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SleekPurpleOnContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Active Batch Analysis",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekPurpleOnContainer
                                )
                                Text(
                                    text = "Automated 360 Subject & Angle Detection",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SleekPurpleOnContainer.copy(alpha = 0.75f)
                                )
                            }
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isAnalyzing) "PROCESSING" else "${clips.size} READY",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = SleekPurpleOnContainer,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { if (isAnalyzing) 0.65f else 1.0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SleekPurple,
                        trackColor = Color.White.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Batch Summary Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ReelCrimson.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(ReelCrimson.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CropPortrait,
                                        contentDescription = null,
                                        tint = ReelCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "$reelsCount Clips",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                    Text(
                                        text = "Reels (9:16)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LandscapeEmerald.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(LandscapeEmerald.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CropLandscape,
                                        contentDescription = null,
                                        tint = LandscapeEmerald,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "$landscapeCount Clips",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                    Text(
                                        text = "Landscape (16:9)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onBatchAnalyzeClick,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("batch_analyze_all_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isAnalyzing
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyzing Batch...", fontWeight = FontWeight.Bold)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Batch Analyze 360",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = onAddClipClick,
                            modifier = Modifier.testTag("add_clip_button"),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White,
                                contentColor = SleekPurpleOnContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Clip",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Import", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Filter Bar with Sleek styling
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rank By:",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                FilterChip(
                    selected = platformFilter == PlatformFilter.ALL,
                    onClick = { onFilterChange(PlatformFilter.ALL) },
                    label = { Text("Default") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SleekPurpleContainer,
                        selectedLabelColor = SleekPurpleOnContainer,
                        containerColor = SleekSurface,
                        labelColor = SleekTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = platformFilter == PlatformFilter.ALL,
                        borderColor = SleekBorder,
                        selectedBorderColor = SleekPurple
                    )
                )

                FilterChip(
                    selected = platformFilter == PlatformFilter.REELS_FIRST,
                    onClick = { onFilterChange(PlatformFilter.REELS_FIRST) },
                    label = { Text("🔥 Reels (9:16)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ReelCrimson.copy(alpha = 0.15f),
                        selectedLabelColor = ReelCrimson,
                        containerColor = SleekSurface,
                        labelColor = SleekTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = platformFilter == PlatformFilter.REELS_FIRST,
                        borderColor = SleekBorder,
                        selectedBorderColor = ReelCrimson
                    )
                )

                FilterChip(
                    selected = platformFilter == PlatformFilter.LANDSCAPE_FIRST,
                    onClick = { onFilterChange(PlatformFilter.LANDSCAPE_FIRST) },
                    label = { Text("🌄 16:9") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LandscapeEmerald.copy(alpha = 0.15f),
                        selectedLabelColor = LandscapeEmerald,
                        containerColor = SleekSurface,
                        labelColor = SleekTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = platformFilter == PlatformFilter.LANDSCAPE_FIRST,
                        borderColor = SleekBorder,
                        selectedBorderColor = LandscapeEmerald
                    )
                )
            }
        }

        // Clip Cards List
        if (filteredClips.isEmpty()) {
            item {
                CardFrame(
                    backgroundColor = SleekSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No clips in this project yet. Tap 'Import' above to add raw 360 clips.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SleekTextSecondary
                        )
                    }
                }
            }
        } else {
            items(filteredClips, key = { it.id }) { clip ->
                ClipRankCard(
                    clip = clip,
                    onInspectClick = onInspectClipClick,
                    onAnalyzeClick = onAnalyzeClipClick
                )
            }
        }
    }
}

