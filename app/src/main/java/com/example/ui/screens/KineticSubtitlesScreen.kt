package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun KineticSubtitlesScreen(
    viewModel: OsmoViewModel,
    modifier: Modifier = Modifier
) {
    val subtitles by viewModel.projectSubtitles.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    var selectedPreset by remember { mutableStateOf("MrBeast Kinetic Pop") }
    var selectedLanguage by remember { mutableStateOf("English") }

    val presets = listOf(
        "MrBeast Kinetic Pop",
        "Hormozi Bold Glow",
        "Minimal Cine Serif",
        "Cyber Neon Glow"
    )

    val languages = listOf(
        "English",
        "Urdu",
        "Pashto"
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
                            .background(Brush.linearGradient(listOf(OsmoYellow, OsmoOrange))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Subtitles,
                            contentDescription = "Subtitles",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Kinetic Subtitles Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Auto-Synced Trilingual Captions (English, Urdu, Pashto)",
                            style = MaterialTheme.typography.bodySmall,
                            color = OsmoYellow
                        )
                    }
                }
            }
        }

        // Live Subtitle Player Preview Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OsmoYellow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kinetic Overlay Preview (9:16 Reels)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated 9:16 Reel frame with kinetic subtitle pop
                    Box(
                        modifier = Modifier
                            .width(220.dp)
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0D1117))
                            .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val activeSubText = subtitles.firstOrNull()?.text ?: "RIDING THE EDGE OF SWAT VALLEY!"
                        Text(
                            text = activeSubText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            color = when (selectedPreset) {
                                "Hormozi Bold Glow" -> OsmoNeonGreen
                                "Cyber Neon Glow" -> OsmoCyan
                                else -> OsmoYellow
                            }
                        )
                    }
                }
            }
        }

        // Subtitle Style Presets
        item {
            Text(
                text = "Kinetic Animation Style",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets) { preset ->
                    val isSelected = selectedPreset == preset
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OsmoYellow else Color(0xFF21262D))
                            .clickable {
                                selectedPreset = preset
                                viewModel.generateSubtitles(preset, selectedLanguage)
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

        // Language Selector
        item {
            Text(
                text = "Language & Script",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                languages.forEach { lang ->
                    val isSelected = selectedLanguage == lang
                    val label = when(lang) {
                        "Urdu" -> "اردو (Urdu)"
                        "Pashto" -> "پښتو (Pashto)"
                        else -> "English (EN)"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OsmoCyan else Color(0xFF21262D))
                            .clickable {
                                selectedLanguage = lang
                                viewModel.generateSubtitles(selectedPreset, lang)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        // Generate Subtitles Button
        item {
            Button(
                onClick = { viewModel.generateSubtitles(selectedPreset, selectedLanguage) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_subtitles_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = OsmoYellow),
                shape = RoundedCornerShape(12.dp),
                enabled = !isAnalyzing
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Syncing Subtitles (Gemini 3.1)...", color = Color.Black, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Kinetic Subtitles", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Subtitles Timecoded Item List
        item {
            Text(
                text = "Generated Caption Timeline Blocks",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(subtitles) { sub ->
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${sub.startTimecode} -> ${sub.endTimecode}",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = OsmoCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sub.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(OsmoYellow.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(sub.stylePreset, style = MaterialTheme.typography.labelSmall, color = OsmoYellow)
                    }
                }
            }
        }
    }
}
