package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
fun ColorGradingScreen(
    viewModel: OsmoViewModel,
    modifier: Modifier = Modifier
) {
    val activeGrade by viewModel.activeColorGrade.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val context = LocalContext.current

    var selectedPreset by remember { mutableStateOf("Teal & Orange Action Blockbuster") }
    var customPrompt by remember { mutableStateOf("") }

    var temp by remember { mutableIntStateOf(activeGrade?.temperature ?: 5600) }
    var tint by remember { mutableIntStateOf(activeGrade?.tint ?: 2) }
    var contrast by remember { mutableFloatStateOf(activeGrade?.contrast ?: 1.15f) }
    var saturation by remember { mutableFloatStateOf(activeGrade?.saturation ?: 1.10f) }

    val presetStyles = listOf(
        "Teal & Orange Action Blockbuster",
        "Moody Emerald Forest",
        "Golden Hour Glow",
        "Cyberpunk Neon High Contrast",
        "Vintage Film 70s Warmth",
        "D-Log M Natural Rec.709"
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
                            .background(Brush.linearGradient(listOf(OsmoOrange, OsmoYellow))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ColorLens,
                            contentDescription = "Color Grade",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI 3D LUT & Color Grading Studio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Powered by Gemini 3.1 Flash Lite (DaVinci Resolve .CUBE)",
                            style = MaterialTheme.typography.bodySmall,
                            color = OsmoOrange
                        )
                    }
                }
            }
        }

        // Active LUT Status & Quick Preview Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OsmoOrange.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
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
                                text = activeGrade?.lutName ?: "Teal & Orange 3D LUT",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = activeGrade?.presetDescription ?: "Film-grade D-Log M conversion curve.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("3D LUT", activeGrade?.cubeLutRawText ?: "")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "DaVinci .CUBE LUT copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF21262D))
                                .testTag("copy_cube_lut_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy .CUBE LUT",
                                tint = OsmoOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Film Color Simulation Palette Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(0xFF0E2A38))) // Deep teal shadow
                        Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(0xFF1B4958)))
                        Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(0xFF686256))) // Neutral midtone
                        Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(0xFFD48B47))) // Warm orange highlight
                        Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(0xFFFFD199))) // Specular roll-off
                    }
                }
            }
        }

        // Preset LUT Styles
        item {
            Text(
                text = "Cinematic LUT Presets",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presetStyles) { style ->
                    val isSelected = selectedPreset == style
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) OsmoOrange else Color(0xFF21262D))
                            .clickable {
                                selectedPreset = style
                                viewModel.generateColorGradeLut(style, customPrompt)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = style,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        // Color Calibration Sliders
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
                    Text(
                        text = "Primary Color Wheels & Tone Adjustments",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Temperature
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("White Balance Temp", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text("${temp}K", style = MaterialTheme.typography.bodySmall, color = OsmoYellow)
                        }
                        Slider(
                            value = temp.toFloat(),
                            onValueChange = { temp = it.toInt() },
                            valueRange = 3200f..8000f,
                            colors = SliderDefaults.colors(thumbColor = OsmoYellow, activeTrackColor = OsmoYellow)
                        )
                    }

                    // Contrast
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Dynamic Contrast", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text(String.format("%.2f", contrast), style = MaterialTheme.typography.bodySmall, color = OsmoCyan)
                        }
                        Slider(
                            value = contrast,
                            onValueChange = { contrast = it },
                            valueRange = 0.8f..1.5f,
                            colors = SliderDefaults.colors(thumbColor = OsmoCyan, activeTrackColor = OsmoCyan)
                        )
                    }

                    // Saturation
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Color Vibrance & Saturation", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text(String.format("%.2f", saturation), style = MaterialTheme.typography.bodySmall, color = OsmoNeonGreen)
                        }
                        Slider(
                            value = saturation,
                            onValueChange = { saturation = it },
                            valueRange = 0.5f..1.8f,
                            colors = SliderDefaults.colors(thumbColor = OsmoNeonGreen, activeTrackColor = OsmoNeonGreen)
                        )
                    }

                    // Custom AI Prompt Box
                    OutlinedTextField(
                        value = customPrompt,
                        onValueChange = { customPrompt = it },
                        placeholder = { Text("Describe specific grade (e.g., Blade Runner cyan dusk with warm skin tones)", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("color_grade_prompt_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OsmoOrange,
                            unfocusedBorderColor = Color(0xFF30363D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.generateColorGradeLut(selectedPreset, customPrompt)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("synthesize_ai_lut_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = OsmoOrange),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isAnalyzing
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Synthesizing 3D LUT (Gemini 3.1)...", color = Color.Black, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Custom 3D LUT (DaVinci .CUBE)", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // DaVinci .CUBE Raw Code Preview
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "DaVinci Resolve .CUBE LUT Export Payload",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeGrade?.cubeLutRawText ?: "# No LUT generated yet",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = OsmoCyan,
                        maxLines = 8
                    )
                }
            }
        }
    }
}
