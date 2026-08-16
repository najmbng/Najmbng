package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun DirectorCopilotScreen(
    viewModel: OsmoViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.copilotMessages.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    var inputPrompt by remember { mutableStateOf("") }

    val quickPrompts = listOf(
        "Generate 15s Viral Reel Strategy",
        "How to transition from Ridge to Street Food?",
        "Recommend DaVinci 360 reframing settings",
        "Best music BPM & sound effect placement"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
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
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Director Copilot",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Director Copilot",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Powered by Gemini 3.1 Pro Preview (Hollywood 360 Lead)",
                        style = MaterialTheme.typography.bodySmall,
                        color = OsmoCyan
                    )
                }
            }
        }

        // Quick prompts suggestions
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickPrompts) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF21262D))
                        .clickable {
                            inputPrompt = prompt
                            viewModel.sendCopilotMessage(prompt)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        color = OsmoCyan
                    )
                }
            }
        }

        // Chat Message Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.isUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) OsmoCyan.copy(alpha = 0.2f) else Color(0xFF161B22)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.88f)
                            .border(
                                1.dp,
                                if (isUser) OsmoCyan.copy(alpha = 0.5f) else Color(0xFF30363D),
                                RoundedCornerShape(14.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isUser) "Editor" else "AI Director (Gemini 3.1 Pro)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUser) OsmoCyan else OsmoPurple
                                )
                                if (!isUser && !msg.actionSuggested.isNullOrEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(OsmoNeonGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = msg.actionSuggested.orEmpty(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = OsmoNeonGreen
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = msg.messageText,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputPrompt,
                onValueChange = { inputPrompt = it },
                placeholder = { Text("Ask Director (e.g. How to reframe Clip 2?)", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("copilot_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OsmoCyan,
                    unfocusedBorderColor = Color(0xFF30363D),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 2
            )

            IconButton(
                onClick = {
                    val prompt = inputPrompt
                    inputPrompt = ""
                    viewModel.sendCopilotMessage(prompt)
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(OsmoCyan)
                    .testTag("send_copilot_btn"),
                enabled = !isAnalyzing && inputPrompt.isNotBlank()
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
                }
            }
        }
    }
}
