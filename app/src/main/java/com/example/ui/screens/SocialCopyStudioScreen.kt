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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SocialCopyEntity
import com.example.ui.components.CardFrame
import com.example.ui.theme.CinemaGold
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialCopyStudioScreen(
    copies: List<SocialCopyEntity>,
    isAnalyzing: Boolean,
    onRegenerateClick: (tone: String) -> Unit
) {
    val context = LocalContext.current
    var selectedLanguageIndex by remember { mutableIntStateOf(0) }
    val languages = listOf("English", "Urdu (اردو)", "Pashto (پښتو)")

    var selectedTone by remember { mutableStateOf("Viral Cinematic") }
    val tones = listOf("Viral Cinematic", "Travel Documentary", "Adventure Action", "Cultural Story")

    val activeCopy = when (selectedLanguageIndex) {
        0 -> copies.find { it.language.equals("English", ignoreCase = true) } ?: copies.getOrNull(0)
        1 -> copies.find { it.language.equals("Urdu", ignoreCase = true) } ?: copies.getOrNull(1)
        else -> copies.find { it.language.equals("Pashto", ignoreCase = true) } ?: copies.getOrNull(2)
    }

    val isRtl = selectedLanguageIndex == 1 || selectedLanguageIndex == 2

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied $label to clipboard!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("social_copy_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header & Tone Selector
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
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                tint = SleekPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Trilingual Social Copy",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                        }

                        Button(
                            onClick = { onRegenerateClick(selectedTone) },
                            modifier = Modifier.testTag("regenerate_social_copy_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isAnalyzing
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Generate AI Copy", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Content Tone Style:",
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tones.forEach { tone ->
                            FilterChip(
                                selected = selectedTone == tone,
                                onClick = { selectedTone = tone },
                                label = { Text(tone, fontSize = 10.sp) },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SleekPurpleContainer,
                                    selectedLabelColor = SleekPurpleOnContainer,
                                    containerColor = SleekSurfaceVariant,
                                    labelColor = SleekTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedTone == tone,
                                    borderColor = if (selectedTone == tone) SleekPurple else SleekBorderSubtle
                                )
                            )
                        }
                    }
                }
            }
        }

        // Language Tabs (English, Urdu, Pashto)
        item {
            Surface(
                color = SleekSurface,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                shadowElevation = 1.dp
            ) {
                TabRow(
                    selectedTabIndex = selectedLanguageIndex,
                    containerColor = SleekSurface,
                    contentColor = SleekPurple,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedLanguageIndex]),
                            color = SleekPurple
                        )
                    }
                ) {
                    languages.forEachIndexed { index, lang ->
                        Tab(
                            selected = selectedLanguageIndex == index,
                            onClick = { selectedLanguageIndex = index },
                            text = {
                                Text(
                                    text = lang,
                                    fontWeight = if (selectedLanguageIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedLanguageIndex == index) SleekPurple else SleekTextSecondary
                                )
                            }
                        )
                    }
                }
            }
        }

        // Copy Content Section
        if (activeCopy == null) {
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
                            text = "No captions generated for ${languages[selectedLanguageIndex]} yet. Tap 'Generate AI Copy'.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SleekTextSecondary
                        )
                    }
                }
            }
        } else {
            // 1. Title Box
            item {
                CopySectionCard(
                    title = "🎬 Title (Reels / Shorts / YouTube)",
                    content = activeCopy.title,
                    isRtl = isRtl,
                    accentColor = SleekPurple,
                    onCopy = { copyToClipboard("Title", activeCopy.title) }
                )
            }

            // 2. 3-Second Hook Box
            item {
                CopySectionCard(
                    title = "⚡ 3-Second Opening Hook / Voiceover",
                    content = activeCopy.hook,
                    isRtl = isRtl,
                    accentColor = CinemaGold,
                    onCopy = { copyToClipboard("Hook", activeCopy.hook) }
                )
            }

            // 3. Social Media Caption
            item {
                CopySectionCard(
                    title = "📱 Social Caption (Instagram / TikTok)",
                    content = activeCopy.caption,
                    isRtl = isRtl,
                    accentColor = SleekPurple,
                    onCopy = { copyToClipboard("Caption", activeCopy.caption) }
                )
            }

            // 4. Call To Action (CTA)
            item {
                CopySectionCard(
                    title = "📢 Call to Action (CTA)",
                    content = activeCopy.callToAction,
                    isRtl = isRtl,
                    accentColor = LandscapeEmerald,
                    onCopy = { copyToClipboard("CTA", activeCopy.callToAction) }
                )
            }

            // 5. Hashtags
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
                            Text(
                                text = "#️⃣ Viral Hashtags",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            FilledTonalButton(
                                onClick = { copyToClipboard("Hashtags", activeCopy.hashtags) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = SleekPurpleContainer,
                                    contentColor = SleekPurpleOnContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy Tags", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            activeCopy.hashtags.split(" ").filter { it.isNotBlank() }.forEach { tag ->
                                Surface(
                                    color = SleekSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle)
                                ) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekPurple,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Full YouTube SEO Description
            item {
                CopySectionCard(
                    title = "🎥 YouTube SEO Description & Timestamps",
                    content = activeCopy.youtubeDescription,
                    isRtl = isRtl,
                    accentColor = SleekPurple,
                    onCopy = { copyToClipboard("YouTube Description", activeCopy.youtubeDescription) }
                )
            }
        }
    }
}

@Composable
fun CopySectionCard(
    title: String,
    content: String,
    isRtl: Boolean,
    accentColor: androidx.compose.ui.graphics.Color = SleekPurple,
    onCopy: () -> Unit
) {
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary
                )

                FilledTonalButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = SleekPurpleContainer,
                        contentColor = SleekPurpleOnContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    text = content,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SleekTextPrimary,
                    lineHeight = 22.sp,
                    textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,
                    fontFamily = if (isRtl) FontFamily.SansSerif else FontFamily.Default
                )
            }
        }
    }
}

