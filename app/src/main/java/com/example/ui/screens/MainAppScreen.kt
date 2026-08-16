package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AddClipDialog
import com.example.ui.components.ExportSheetDialog
import com.example.ui.components.NewProjectDialog
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.DaVinciPurple
import com.example.ui.theme.LandscapeEmerald
import com.example.ui.theme.OsmoCyan
import com.example.ui.theme.OsmoCyanGlow
import com.example.ui.theme.OsmoNeonGreen
import com.example.ui.theme.OsmoOrange
import com.example.ui.theme.OsmoPurple
import com.example.ui.theme.OsmoTeal
import com.example.ui.theme.OsmoYellow
import com.example.ui.theme.ReelCrimson
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.SleekBg
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleContainer
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurpleOnContainer
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekSurfaceContainer
import com.example.ui.theme.SleekSurfaceVariant
import com.example.ui.theme.SleekTextMuted
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.OsmoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: OsmoViewModel) {
    val activeProject by viewModel.activeProject.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()
    val clips by viewModel.projectClips.collectAsState()
    val keyframes by viewModel.activeClipKeyframes.collectAsState()
    val selectedClip by viewModel.selectedClip.collectAsState()
    val selectedKeyframe by viewModel.selectedKeyframe.collectAsState()
    val edlItems by viewModel.projectEdlItems.collectAsState()
    val socialCopies by viewModel.projectSocialCopies.collectAsState()
    val script by viewModel.projectScript.collectAsState()

    val selectedTab by viewModel.selectedTab.collectAsState()
    val platformFilter by viewModel.platformFilter.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val showExportDialog by viewModel.showExportDialog.collectAsState()
    val showNewProjectDialog by viewModel.showNewProjectDialog.collectAsState()
    val showAddClipDialog by viewModel.showAddClipDialog.collectAsState()

    var projectDropdownExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val featureScrollState = rememberScrollState()

    LaunchedEffect(statusMessage) {
        statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = Modifier.testTag("main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                Surface(
                    color = SleekSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                    shadowElevation = 1.dp
                ) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // App Brand Logo icon
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(SleekPurple, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "360",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "OsmoFlow 360",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Black,
                                            color = SleekTextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = SleekPurpleContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "PRO RESOLVE",
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SleekPurpleOnContainer,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    // Project Selector Pill
                                    Box {
                                        Row(
                                            modifier = Modifier
                                                .clickable { projectDropdownExpanded = true }
                                                .padding(vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = activeProject?.title ?: "Select Project",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = SleekPurple,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Select Project",
                                                tint = SleekPurple,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = projectDropdownExpanded,
                                            onDismissRequest = { projectDropdownExpanded = false },
                                            modifier = Modifier.background(SleekSurface)
                                        ) {
                                            allProjects.forEach { proj ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Column {
                                                            Text(
                                                                text = proj.title,
                                                                color = if (proj.id == activeProject?.id) SleekPurple else SleekTextPrimary,
                                                                fontWeight = if (proj.id == activeProject?.id) FontWeight.Bold else FontWeight.Normal
                                                            )
                                                            Text(
                                                                text = "${proj.preferredPacing} • ${proj.targetFramerate} FPS",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                color = SleekTextSecondary
                                                            )
                                                        }
                                                    },
                                                    onClick = {
                                                        viewModel.selectProject(proj.id)
                                                        projectDropdownExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.setShowNewProjectDialog(true) },
                                modifier = Modifier.testTag("top_bar_new_project_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Project",
                                    tint = SleekPurple
                                )
                            }

                            IconButton(
                                onClick = { viewModel.setShowExportDialog(true) },
                                modifier = Modifier.testTag("top_bar_export_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileDownload,
                                    contentDescription = "Export DaVinci Resolve",
                                    tint = CinemaGold
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = SleekSurface,
                            titleContentColor = SleekTextPrimary
                        )
                    )
                }

                // Advanced Editing Features Quick Ribbon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F141C))
                        .horizontalScroll(featureScrollState)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf(
                        Triple(AppTab.BATCH_RANKING, "Rank Matrix", Icons.Default.Layers),
                        Triple(AppTab.CLIP_INSPECTOR, "360 Angles", Icons.Default.Visibility),
                        Triple(AppTab.EDL_DAVINCI, "Resolve EDL", Icons.Default.ContentCut),
                        Triple(AppTab.AI_SOUNDTRACK, "AI Music (Lyria)", Icons.Default.GraphicEq),
                        Triple(AppTab.COLOR_GRADING_LUT, "Color 3D LUT", Icons.Default.ColorLens),
                        Triple(AppTab.SPEED_RAMP_STUDIO, "Speed Ramp", Icons.Default.Speed),
                        Triple(AppTab.KINETIC_SUBTITLES, "Kinetic Subs", Icons.Default.Subtitles),
                        Triple(AppTab.SOCIAL_COPY, "3-Lang Copy", Icons.Default.Translate),
                        Triple(AppTab.DIRECTOR_PLAN, "Director Cut", Icons.Default.Description),
                        Triple(AppTab.DIRECTOR_COPILOT, "AI Director (3.1 Pro)", Icons.Default.Videocam)
                    )

                    tabs.forEach { (tab, title, icon) ->
                        val isSelected = selectedTab == tab
                        val chipColor = when (tab) {
                            AppTab.AI_SOUNDTRACK -> OsmoCyan
                            AppTab.COLOR_GRADING_LUT -> OsmoOrange
                            AppTab.SPEED_RAMP_STUDIO -> OsmoNeonGreen
                            AppTab.KINETIC_SUBTITLES -> OsmoYellow
                            AppTab.DIRECTOR_COPILOT -> OsmoPurple
                            else -> SleekPurple
                        }

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) chipColor else Color(0xFF1E2530))
                                .clickable { viewModel.selectTab(tab) }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (isSelected) Color.Black else Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SleekSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle),
                shadowElevation = 2.dp
            ) {
                NavigationBar(
                    containerColor = SleekSurface,
                    contentColor = SleekPurple
                ) {
                    NavigationBarItem(
                        selected = selectedTab == AppTab.BATCH_RANKING,
                        onClick = { viewModel.selectTab(AppTab.BATCH_RANKING) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Batch Rank"
                            )
                        },
                        label = { Text("Rank", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SleekPurpleOnContainer,
                            selectedTextColor = SleekPurpleOnContainer,
                            indicatorColor = SleekPurpleContainer,
                            unselectedIconColor = SleekTextSecondary,
                            unselectedTextColor = SleekTextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == AppTab.CLIP_INSPECTOR,
                        onClick = { viewModel.selectTab(AppTab.CLIP_INSPECTOR) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "360 Inspector"
                            )
                        },
                        label = { Text("Angles", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SleekPurpleOnContainer,
                            selectedTextColor = SleekPurpleOnContainer,
                            indicatorColor = SleekPurpleContainer,
                            unselectedIconColor = SleekTextSecondary,
                            unselectedTextColor = SleekTextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == AppTab.AI_SOUNDTRACK,
                        onClick = { viewModel.selectTab(AppTab.AI_SOUNDTRACK) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "AI Music"
                            )
                        },
                        label = { Text("Music", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = OsmoCyan,
                            indicatorColor = OsmoCyan,
                            unselectedIconColor = SleekTextSecondary,
                            unselectedTextColor = SleekTextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == AppTab.COLOR_GRADING_LUT,
                        onClick = { viewModel.selectTab(AppTab.COLOR_GRADING_LUT) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = "3D LUT"
                            )
                        },
                        label = { Text("Color", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = OsmoOrange,
                            indicatorColor = OsmoOrange,
                            unselectedIconColor = SleekTextSecondary,
                            unselectedTextColor = SleekTextSecondary
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == AppTab.DIRECTOR_COPILOT,
                        onClick = { viewModel.selectTab(AppTab.DIRECTOR_COPILOT) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "AI Director"
                            )
                        },
                        label = { Text("Director", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = OsmoPurple,
                            indicatorColor = OsmoPurple,
                            unselectedIconColor = SleekTextSecondary,
                            unselectedTextColor = SleekTextSecondary
                        )
                    )
                }
            }
        },
        containerColor = SleekBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.BATCH_RANKING -> {
                    BatchRankingScreen(
                        clips = clips,
                        platformFilter = platformFilter,
                        isAnalyzing = isAnalyzing,
                        onFilterChange = { viewModel.setPlatformFilter(it) },
                        onBatchAnalyzeClick = { viewModel.batchAnalyzeProject() },
                        onAddClipClick = { viewModel.setShowAddClipDialog(true) },
                        onInspectClipClick = { viewModel.selectClip(it) },
                        onAnalyzeClipClick = { viewModel.analyzeSingleClip(it) }
                    )
                }

                AppTab.CLIP_INSPECTOR -> {
                    ClipInspectorScreen(
                        clip = selectedClip ?: clips.firstOrNull(),
                        keyframes = keyframes,
                        selectedKeyframe = selectedKeyframe,
                        isAnalyzing = isAnalyzing,
                        onKeyframeSelect = { viewModel.selectKeyframe(it) },
                        onAnalyzeClipClick = { viewModel.analyzeSingleClip(it) }
                    )
                }

                AppTab.EDL_DAVINCI -> {
                    EdlStudioScreen(
                        project = activeProject,
                        edlItems = edlItems,
                        isAnalyzing = isAnalyzing,
                        onRegenerateEdlClick = {
                            activeProject?.let { viewModel.batchAnalyzeProject() }
                        },
                        onExportDaVinciClick = { viewModel.setShowExportDialog(true) }
                    )
                }

                AppTab.AI_SOUNDTRACK -> {
                    SoundtrackStudioScreen(viewModel = viewModel)
                }

                AppTab.COLOR_GRADING_LUT -> {
                    ColorGradingScreen(viewModel = viewModel)
                }

                AppTab.SPEED_RAMP_STUDIO -> {
                    SpeedRampStudioScreen(viewModel = viewModel)
                }

                AppTab.KINETIC_SUBTITLES -> {
                    KineticSubtitlesScreen(viewModel = viewModel)
                }

                AppTab.SOCIAL_COPY -> {
                    SocialCopyStudioScreen(
                        copies = socialCopies,
                        isAnalyzing = isAnalyzing,
                        onRegenerateClick = { tone -> viewModel.regenerateSocialCopy(tone) }
                    )
                }

                AppTab.DIRECTOR_PLAN -> {
                    DirectorPlanScreen(
                        script = script,
                        isAnalyzing = isAnalyzing,
                        onRegeneratePlanClick = { viewModel.regenerateDirectorScript() },
                        onOpenExportSheetClick = { viewModel.setShowExportDialog(true) }
                    )
                }

                AppTab.DIRECTOR_COPILOT -> {
                    DirectorCopilotScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        ExportSheetDialog(
            script = script,
            onDismiss = { viewModel.setShowExportDialog(false) }
        )
    }

    // New Project Dialog
    if (showNewProjectDialog) {
        NewProjectDialog(
            onDismiss = { viewModel.setShowNewProjectDialog(false) },
            onCreate = { title, desc, pacing ->
                viewModel.createNewProject(title, desc, pacing)
            }
        )
    }

    // Add Clip Dialog
    if (showAddClipDialog) {
        AddClipDialog(
            onDismiss = { viewModel.setShowAddClipDialog(false) },
            onAdd = { name, dur, desc, motion ->
                viewModel.addClipToActiveProject(name, dur, desc, motion)
            }
        )
    }
}

