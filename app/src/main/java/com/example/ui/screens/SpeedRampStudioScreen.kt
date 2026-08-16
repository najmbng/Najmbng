package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.OsmoCyan
import com.example.ui.theme.OsmoDarkSurface
import com.example.ui.theme.OsmoNeonGreen
import com.example.ui.theme.OsmoOrange
import com.example.ui.theme.OsmoPurple
import com.example.ui.theme.OsmoYellow
import com.example.ui.viewmodel.OsmoViewModel

@Composable
fun SpeedRampStudioScreen(
    viewModel: OsmoViewModel,
    modifier: Modifier = Modifier
) {
    val speedRamps by viewModel.projectSpeedRamps.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    var selectedStyle by remember { mutableStateOf("Whip-Pan Snap") }
    var opticalFlowEnabled by remember { mutableStateOf(true) }
    var motionBlurStrength by remember { mutableFloatStateOf(80f) }

    val rampPresets = listOf(
        "Whip-Pan Snap",
        "Slow-Mo Impact",
        "Hyper-Smooth Flow",
        "Bullet Time Freeze"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OsmoDarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(OsmoNeonGreen, OsmoCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed Ramp Studio",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Speed Ramping & Motion Curve",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Bezier Dynamic Easing & Optical Flow Interpolation",
                            style = MaterialTheme.typography.bodySmall,
                            color = OsmoNeonGreen
                        )
                    }
                }
            }
        }

        // Interactive Velocity Curve Canvas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OsmoNeonGreen.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Velocity Curve (Kinetic Bezier)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = selectedStyle,
                            style = MaterialTheme.typography.labelSmall,
                            color = OsmoNeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    SpeedCurveCanvas(
                        style = selectedStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1117))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("In: 1.0x Normal", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("Peak: 4.0x Whip", style = MaterialTheme.typography.labelSmall, color = OsmoOrange)
                        Text("Impact: 0.25x Slowmo", style = MaterialTheme.typography.labelSmall, color = OsmoCyan)
                        Text("Out: 1.0x Flow", style = MaterialTheme.typography.labelSmall, color = OsmoNeonGreen)
                    }
                }
            }
        }

        // Presets selector
        item {
            Text(
                text = "Dynamic Speed Profiles",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rampPresets) { preset ->
                    val isSelected = selectedStyle == preset
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OsmoNeonGreen else Color(0xFF21262D))
                            .clickable {
                                selectedStyle = preset
                                viewModel.generateSpeedRamps(preset)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        // Motion blur & optical flow controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OsmoDarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Optical Flow AI Interpolation", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text("Generates 120fps intermediate frames for butter-smooth slow motion", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                        Switch(
                            checked = opticalFlowEnabled,
                            onCheckedChange = { opticalFlowEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = OsmoNeonGreen)
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Kinetic Motion Blur Shutter", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text("${motionBlurStrength.toInt()}%", style = MaterialTheme.typography.bodySmall, color = OsmoCyan)
                        }
                        Slider(
                            value = motionBlurStrength,
                            onValueChange = { motionBlurStrength = it },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = OsmoCyan, activeTrackColor = OsmoCyan)
                        )
                    }

                    Button(
                        onClick = { viewModel.generateSpeedRamps(selectedStyle) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_speed_ramps_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = OsmoNeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isAnalyzing
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calculating Bezier Math (Gemini 3.1)...", color = Color.Black, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Apply Speed Ramps to Timeline", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Speed Ramps for Clips
        item {
            Text(
                text = "Applied Clip Speed Curves",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(speedRamps) { ramp ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(ramp.clipName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Curve: ${ramp.rampType} • ${ramp.curveEasing}", style = MaterialTheme.typography.labelSmall, color = OsmoNeonGreen)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(OsmoCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Optical Flow 120fps", style = MaterialTheme.typography.labelSmall, color = OsmoCyan)
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedCurveCanvas(
    style: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Grid lines
        for (i in 1..3) {
            val y = h * (i / 4f)
            drawLine(
                color = Color(0xFF21262D),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
        }

        val path = Path()
        when (style) {
            "Slow-Mo Impact" -> {
                path.moveTo(0f, h * 0.5f)
                path.cubicTo(w * 0.25f, h * 0.5f, w * 0.35f, h * 0.85f, w * 0.5f, h * 0.85f)
                path.cubicTo(w * 0.65f, h * 0.85f, w * 0.75f, h * 0.5f, w, h * 0.5f)
            }
            "Hyper-Smooth Flow" -> {
                path.moveTo(0f, h * 0.6f)
                path.cubicTo(w * 0.3f, h * 0.6f, w * 0.4f, h * 0.3f, w * 0.6f, h * 0.3f)
                path.cubicTo(w * 0.8f, h * 0.3f, w * 0.9f, h * 0.6f, w, h * 0.6f)
            }
            else -> { // Whip-Pan Snap
                path.moveTo(0f, h * 0.5f)
                path.cubicTo(w * 0.2f, h * 0.5f, w * 0.3f, h * 0.15f, w * 0.45f, h * 0.15f)
                path.cubicTo(w * 0.55f, h * 0.15f, w * 0.65f, h * 0.85f, w * 0.75f, h * 0.85f)
                path.cubicTo(w * 0.85f, h * 0.85f, w * 0.95f, h * 0.5f, w, h * 0.5f)
            }
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(listOf(OsmoCyan, OsmoOrange, OsmoNeonGreen)),
            style = Stroke(width = 4f)
        )

        // Draw keyframe nodes
        drawCircle(color = OsmoCyan, radius = 6f, center = Offset(w * 0.2f, h * 0.5f))
        drawCircle(color = OsmoOrange, radius = 7f, center = Offset(w * 0.45f, h * 0.15f))
        drawCircle(color = OsmoYellow, radius = 7f, center = Offset(w * 0.75f, h * 0.85f))
    }
}
