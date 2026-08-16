package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.OsmoTeal
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
fun NewProjectDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String, pacing: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var pacing by remember { mutableStateOf("Dynamic Social") }
    val pacingOptions = listOf("Fast Action", "Dynamic Social", "Cinematic Slow", "Documentary")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SleekSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("new_project_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New 360 Footage Project",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SleekTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title (e.g. Hunza Valley 360 Expedition)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_project_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        focusedContainerColor = SleekSurfaceVariant,
                        unfocusedContainerColor = SleekSurfaceVariant,
                        focusedLabelColor = SleekPurple,
                        unfocusedLabelColor = SleekTextSecondary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Location Context") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_project_desc_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        focusedContainerColor = SleekSurfaceVariant,
                        unfocusedContainerColor = SleekSurfaceVariant,
                        focusedLabelColor = SleekPurple,
                        unfocusedLabelColor = SleekTextSecondary
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Edit Pacing Style:",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pacingOptions.take(2).forEach { opt ->
                        FilterChip(
                            selected = pacing == opt,
                            onClick = { pacing = opt },
                            label = { Text(opt, fontSize = 10.sp) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SleekPurpleContainer,
                                selectedLabelColor = SleekPurpleOnContainer,
                                containerColor = SleekSurfaceVariant,
                                labelColor = SleekTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = pacing == opt,
                                borderColor = if (pacing == opt) SleekPurple else SleekBorderSubtle
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pacingOptions.drop(2).forEach { opt ->
                        FilterChip(
                            selected = pacing == opt,
                            onClick = { pacing = opt },
                            label = { Text(opt, fontSize = 10.sp) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SleekPurpleContainer,
                                selectedLabelColor = SleekPurpleOnContainer,
                                containerColor = SleekSurfaceVariant,
                                labelColor = SleekTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = pacing == opt,
                                borderColor = if (pacing == opt) SleekPurple else SleekBorderSubtle
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onCreate(title.trim(), description.trim(), pacing)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_project_confirm_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = title.isNotBlank()
                ) {
                    Text("Create Project & Start 360 Ingest", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AddClipDialog(
    onDismiss: () -> Unit,
    onAdd: (fileName: String, durationSec: Float, sceneDesc: String, motion: String) -> Unit
) {
    var fileName by remember { mutableStateOf("DJI_360_000" + (1..9).random() + ".mp4") }
    var durationText by remember { mutableStateOf("18.5") }
    var sceneDesc by remember { mutableStateOf("") }
    var motion by remember { mutableStateOf("Kinetic Selfie Chase") }

    val motionOptions = listOf(
        "Kinetic Selfie Chase",
        "Tiny Planet Orbit",
        "Scenic Landscape Pan",
        "Extreme Action Point",
        "Walking POV"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SleekSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_clip_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Import Raw 360 Clip",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SleekTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Clip File Name") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_clip_filename_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        focusedContainerColor = SleekSurfaceVariant,
                        unfocusedContainerColor = SleekSurfaceVariant,
                        focusedLabelColor = SleekPurple,
                        unfocusedLabelColor = SleekTextSecondary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("Duration in Seconds") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_clip_duration_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        focusedContainerColor = SleekSurfaceVariant,
                        unfocusedContainerColor = SleekSurfaceVariant,
                        focusedLabelColor = SleekPurple,
                        unfocusedLabelColor = SleekTextSecondary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = sceneDesc,
                    onValueChange = { sceneDesc = it },
                    label = { Text("Visual Scene Description") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_clip_scenedesc_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary,
                        focusedContainerColor = SleekSurfaceVariant,
                        unfocusedContainerColor = SleekSurfaceVariant,
                        focusedLabelColor = SleekPurple,
                        unfocusedLabelColor = SleekTextSecondary
                    ),
                    placeholder = { Text("e.g. Walking across suspension bridge with mountain backdrop") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Motion Dynamics Preset:",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    motionOptions.take(2).forEach { m ->
                        FilterChip(
                            selected = motion == m,
                            onClick = { motion = m },
                            label = { Text(m, fontSize = 9.sp) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SleekPurpleContainer,
                                selectedLabelColor = SleekPurpleOnContainer,
                                containerColor = SleekSurfaceVariant,
                                labelColor = SleekTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = motion == m,
                                borderColor = if (motion == m) SleekPurple else SleekBorderSubtle
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val dur = durationText.toFloatOrNull() ?: 15.0f
                        onAdd(fileName.trim(), dur, sceneDesc.trim(), motion)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_clip_confirm_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ingest & Analyze 360 Moment", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

