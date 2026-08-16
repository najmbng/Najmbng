package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.EdlItemEntity
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
fun EdlTimelineStrip(
    edlItems: List<EdlItemEntity>,
    modifier: Modifier = Modifier,
    onItemClick: (EdlItemEntity) -> Unit = {}
) {
    CardFrame(
        backgroundColor = SleekSurface,
        borderColor = SleekBorder,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = "EDL Cuts",
                        tint = SleekPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAVINCI RESOLVE EDL TIMELINE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple
                    )
                }

                val totalDuration = edlItems.sumOf { it.durationSeconds.toDouble() }
                Text(
                    text = "${edlItems.size} Cuts | ${String.format("%.1fs", totalDuration)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (edlItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(SleekSurfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No EDL cuts generated yet. Run AI analysis to build rough cut.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SleekTextSecondary
                    )
                }
            } else {
                // Horizontal Timeline Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    edlItems.forEach { item ->
                        EdlTimelineBlock(item = item, onClick = { onItemClick(item) })
                    }
                }
            }
        }
    }
}

@Composable
fun EdlTimelineBlock(
    item: EdlItemEntity,
    onClick: () -> Unit
) {
    val markerColor = when (item.resolveMarkerColor.lowercase()) {
        "cyan" -> SleekPurple
        "green" -> LandscapeEmerald
        "orange" -> CinemaGold
        "magenta" -> ReelCrimson
        else -> DaVinciPurple
    }

    Surface(
        onClick = onClick,
        color = SleekSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, markerColor.copy(alpha = 0.5f)),
        modifier = Modifier
            .width(175.dp)
            .testTag("edl_block_${item.sequenceOrder}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Sequence Number & Color indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(markerColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CUT #${item.sequenceOrder}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                }

                Surface(
                    color = SleekSurface,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle)
                ) {
                    Text(
                        text = "${item.durationSeconds}s",
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = CinemaGold,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Clip Name
            Text(
                text = item.clipName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = SleekPurple,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // In/Out timecode
            Text(
                text = "${item.sourceInTimecode} ➔ ${item.sourceOutTimecode}",
                style = MaterialTheme.typography.labelSmall,
                color = SleekTextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 360 Reframe Directive
            Text(
                text = item.cameraReframeDirective,
                style = MaterialTheme.typography.bodySmall,
                color = SleekTextPrimary,
                fontSize = 10.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Speed Ramp Tag
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = CinemaGold,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.speedRamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = CinemaGold,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

