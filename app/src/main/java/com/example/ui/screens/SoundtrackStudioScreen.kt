package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OsmoCyan
import com.example.ui.theme.OsmoDarkSurface
import com.example.ui.theme.OsmoNeonGreen
import com.example.ui.theme.OsmoOrange
import com.example.ui.theme.OsmoPurple
import com.example.ui.theme.OsmoYellow
import com.example.ui.viewmodel.OsmoViewModel

@Composable
fun SoundtrackStudioScreen(
    viewModel: OsmoViewModel,
    modifier: Modifier = Modifier
) {
    val activeTrack by viewModel.activeMusicTrack.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    var selectedGenre by remember { mutableStateOf("Cinematic Action") }
    var customPrompt by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(30) }
    var useProModel by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var duckingLevel by remember { mutableFloatStateOf(-6.0f) }

    val genres = listOf(
        "Cinematic Action",
        "Lo-Fi Chill",
        "Cyberpunk Synth",
        "Dramatic Drone",
        "Upbeat Vlog"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Header
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
                            .background(Brush.linearGradient(listOf(OsmoPurple, OsmoCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Lyria Studio",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Soundtrack Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Powered by Google Lyria 3 (Clip & Pro Preview)",
                            style = MaterialTheme.typography.bodySmall,
                            color = OsmoCyan
                        )
                    }
                }
            }
        }

        // Active Track Visualizer & Player
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OsmoCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
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
                        Column {
                            Text(
                                text = activeTrack?.title ?: "No AI Soundtrack Loaded",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Model: ${activeTrack?.modelUsed ?: "lyria-3-clip-preview"} • BPM: ${activeTrack?.bpm ?: 124} • Energy: ${activeTrack?.audioEnergy ?: "High"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = OsmoYellow
                            )
                        }

                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) OsmoOrange else OsmoCyan)
                                .testTag("play_pause_soundtrack_btn")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Audio Waveform Canvas
                    AudioWaveformVisualizer(
                        isPlaying = isPlaying,
                        waveformRaw = activeTrack?.waveformPoints ?: "30,60,80,100,70,50,90,100,60,40,80,95,70",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1117))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Timeline Beat Sync info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(OsmoNeonGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Beat Sync Active (Aligned to 360 cuts)",
                                style = MaterialTheme.typography.labelSmall,
                                color = OsmoNeonGreen
                            )
                        }

                        Text(
                            text = "${activeTrack?.durationSeconds ?: 30}s Timeline Track",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        // Genre / Mood Selector
        item {
            Text(
                text = "Soundtrack Mood & Style",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(genres) { genre ->
                    val isSelected = selectedGenre == genre
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OsmoCyan else Color(0xFF21262D))
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = genre,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        // Custom Prompt & Model Tier Settings
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Lyria Generation Prompt",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = customPrompt,
                        onValueChange = { customPrompt = it },
                        placeholder = { Text("e.g., Heavy 808 bass, dramatic orchestral brass, fast tempo drop at 7s", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("lyria_prompt_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OsmoCyan,
                            unfocusedBorderColor = Color(0xFF30363D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Duration: ${selectedDuration}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Text(
                                text = if (selectedDuration <= 30) "Model: lyria-3-clip-preview" else "Model: lyria-3-pro-preview",
                                style = MaterialTheme.typography.labelSmall,
                                color = OsmoCyan
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(15, 30, 60).forEach { dur ->
                                val isSel = selectedDuration == dur
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) OsmoPurple else Color(0xFF21262D))
                                        .clickable {
                                            selectedDuration = dur
                                            useProModel = dur > 30
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${dur}s",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Audio Ducking Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Auto Voice Ducking",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Text(
                                text = "${duckingLevel.toInt()} dB",
                                style = MaterialTheme.typography.bodySmall,
                                color = OsmoOrange
                            )
                        }
                        Slider(
                            value = duckingLevel,
                            onValueChange = { duckingLevel = it },
                            valueRange = -18f..0f,
                            colors = SliderDefaults.colors(
                                thumbColor = OsmoOrange,
                                activeTrackColor = OsmoOrange
                            )
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.generateLyriaMusic(
                                genreMood = selectedGenre,
                                prompt = customPrompt,
                                durationSec = selectedDuration,
                                isPro = useProModel
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_lyria_soundtrack_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = OsmoCyan),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isAnalyzing
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generating Audio with Lyria...", color = Color.Black, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate AI Soundtrack (Lyria 3)", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AudioWaveformVisualizer(
    isPlaying: Boolean,
    waveformRaw: String,
    modifier: Modifier = Modifier
) {
    val heights = remember(waveformRaw) {
        waveformRaw.split(",")
            .mapNotNull { it.trim().toFloatOrNull() }
            .ifEmpty { listOf(30f, 60f, 90f, 70f, 40f, 85f, 100f, 65f, 50f, 80f, 95f, 70f) }
    }

    val phase = remember { Animatable(0f) }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            phase.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            phase.snapTo(0f)
        }
    }

    Canvas(modifier = modifier) {
        val barCount = heights.size
        val barWidth = size.width / (barCount * 1.5f)
        val gap = barWidth * 0.5f

        for (i in 0 until barCount) {
            val baseH = (heights[i] / 100f) * (size.height * 0.8f)
            val animatedH = if (isPlaying) {
                val waveOffset = kotlin.math.sin((phase.value * 2 * Math.PI + i * 0.5)).toFloat() * 10f
                (baseH + waveOffset).coerceIn(4f, size.height)
            } else {
                baseH
            }

            val x = i * (barWidth + gap) + gap
            val y = (size.height - animatedH) / 2f

            drawRoundRect(
                brush = Brush.verticalGradient(
                    listOf(OsmoCyan, OsmoPurple)
                ),
                topLeft = Offset(x, y),
                size = Size(barWidth, animatedH),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}
