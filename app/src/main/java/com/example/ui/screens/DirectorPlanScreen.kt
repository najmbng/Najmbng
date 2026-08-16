package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EditorScriptEntity
import com.example.ui.components.CardFrame
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
fun DirectorPlanScreen(
    script: EditorScriptEntity?,
    isAnalyzing: Boolean,
    onRegeneratePlanClick: () -> Unit,
    onOpenExportSheetClick: () -> Unit
) {
    val context = LocalContext.current

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied $label to clipboard!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("director_plan_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Actions Card
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
                                text = "Director's Editing Plan & Handoff",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            Text(
                                text = "Blueprint for DaVinci Resolve or real human editor",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekTextSecondary
                            )
                        }

                        Surface(
                            color = SleekPurpleContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "EST: ${script?.estimatedDuration ?: "00:45"}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SleekPurpleOnContainer,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenExportSheetClick,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("open_export_sheet_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export / Copy Scripts", fontWeight = FontWeight.Bold)
                        }

                        FilledTonalButton(
                            onClick = onRegeneratePlanClick,
                            modifier = Modifier.testTag("regenerate_director_plan_button"),
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
                                Text("Regenerate", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        if (script == null) {
            item {
                CardFrame(
                    backgroundColor = SleekSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No editing plan synthesized yet. Tap 'Regenerate' to create.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SleekTextSecondary
                        )
                    }
                }
            }
        } else {
            // 1. Narrative Arc Card
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
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    tint = CinemaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "1. Narrative Arc & Progression",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = script.narrativeArc,
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SleekTextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // 2. Beat-by-Beat Reframe & Cut Plan
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
                                    imageVector = Icons.Default.MovieCreation,
                                    contentDescription = null,
                                    tint = SleekPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "2. Beat-by-Beat 360 Reframe Sequence",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = script.beatByBeatPlan,
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SleekTextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // 3. DaVinci Resolve D-Log M Color Grading Notes
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
                                    imageVector = Icons.Default.ColorLens,
                                    contentDescription = null,
                                    tint = DaVinciPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "3. DaVinci Resolve Color Grade (D-Log M)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = script.colorGradingGuidelines,
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SleekTextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // 4. Sound Design & Audio Cues
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
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = LandscapeEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "4. Audio, Whoosh SFX & VO Cues",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = SleekSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = script.soundDesignCues,
                                modifier = Modifier.padding(14.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SleekTextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

