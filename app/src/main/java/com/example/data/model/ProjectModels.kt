package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val targetFramerate: Int = 30, // 30, 60, 24
    val preferredPacing: String = "Dynamic Social", // Dynamic Social, Cinematic Narrative, Fast Cut Action
    val cameraProfile: String = "DJI Osmo 360 D-Log M"
)

@Entity(
    tableName = "clips",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class ClipEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val fileName: String,
    val durationSeconds: Float,
    val resolution: String = "5.7K 360",
    val fps: Int = 30,
    val thumbnailUri: String? = null,
    val sceneDescription: String = "",
    val reelsScore: Int = 85, // 0 - 100
    val landscapeScore: Int = 80, // 0 - 100
    val bestPlatform: String = "Reels (9:16)", // Reels (9:16), Landscape (16:9), Both
    val motionDynamics: String = "High Kinetic", // Static, Gentle Pan, High Kinetic, Orbit
    val keyMomentsCount: Int = 3,
    val isAnalyzed: Boolean = true,
    val audioEnergy: String = "High", // Low, Medium, High
    val gyroStability: String = "RockSteady / HorizonSteady",
    val orderIndex: Int = 0
)

@Entity(
    tableName = "keyframe_angles",
    foreignKeys = [
        ForeignKey(
            entity = ClipEntity::class,
            parentColumns = ["id"],
            childColumns = ["clipId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clipId")]
)
data class KeyframeAngleEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val clipId: String,
    val timestampSeconds: Float,
    val timestampFormatted: String, // e.g. "00:04.20"
    val timecodeIn: String, // "00:00:04:06"
    val timecodeOut: String, // "00:00:08:12"
    val yawDegrees: Float, // -180 to +180
    val pitchDegrees: Float, // -90 to +90
    val rollDegrees: Float = 0f,
    val fovDegrees: Float = 110f, // 60 to 150
    val framingType: String, // "Selfie Tracking", "Forward Chase POV", "Tiny Planet Reveal", "Over-the-Shoulder", "180° Snap Turn"
    val momentTitle: String,
    val reasoning: String,
    val qualityScore: Int = 92,
    val recommendedReframe: String = "Reframe to 9:16 with smooth ease-in-out"
)

@Entity(
    tableName = "edl_items",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class EdlItemEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val clipId: String,
    val clipName: String,
    val sequenceOrder: Int,
    val sourceInTimecode: String,
    val sourceOutTimecode: String,
    val recordInTimecode: String,
    val recordOutTimecode: String,
    val durationSeconds: Float,
    val transition: String = "CUT", // CUT, DISSOLVE 12F, SPEED_RAMP_WHIP
    val cameraReframeDirective: String,
    val resolveMarkerColor: String = "Cyan", // Cyan, Green, Orange, Magenta
    val speedRamp: String = "100%", // e.g. "200% -> 50% SlowMo"
    val gradingNote: String = "D-Log M +0.3 EV, Warm Highlights"
)

@Entity(
    tableName = "social_copies",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class SocialCopyEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val language: String, // "English", "Urdu", "Pashto"
    val title: String,
    val hook: String,
    val caption: String,
    val callToAction: String,
    val hashtags: String,
    val youtubeDescription: String,
    val generatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "editor_scripts",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class EditorScriptEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val targetPlatform: String = "DaVinci Resolve & Human Editor",
    val estimatedDuration: String = "00:45",
    val narrativeArc: String,
    val beatByBeatPlan: String,
    val soundDesignCues: String,
    val colorGradingGuidelines: String,
    val fullMarkdownText: String,
    val daVinciResolvePythonScript: String,
    val cmxEdlRawText: String,
    val csvMarkersRawText: String,
    val generatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "music_tracks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class MusicTrackEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val genreMood: String, // "Cinematic Action", "Lo-Fi Chill", "Cyberpunk Synth", "Dramatic Drone", "Upbeat Vlog"
    val bpm: Int = 124,
    val durationSeconds: Int = 30,
    val modelUsed: String = "lyria-3-clip-preview", // "lyria-3-clip-preview" or "lyria-3-pro-preview"
    val prompt: String,
    val waveformPoints: String = "20,45,60,80,95,70,55,40,65,85,100,75,60,45,30,50,70,90,65,40",
    val audioEnergy: String = "High",
    val duckingLevelDb: Float = -6.0f,
    val isAttachedToTimeline: Boolean = true,
    val generatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "color_grades",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class ColorGradeEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val lutName: String,
    val presetDescription: String,
    val temperature: Int = 5600, // Kelvin or offset (-100 to +100)
    val tint: Int = 4, // -100 to +100
    val exposure: Float = 0.3f, // -2.0 to +2.0 EV
    val contrast: Float = 1.15f,
    val saturation: Float = 1.10f,
    val liftR: Float = -0.02f,
    val liftG: Float = 0.01f,
    val liftB: Float = 0.04f, // Cool shadows
    val gammaR: Float = 0.02f,
    val gammaG: Float = 0.00f,
    val gammaB: Float = -0.01f,
    val gainR: Float = 0.05f,
    val gainG: Float = 0.02f,
    val gainB: Float = -0.04f, // Warm highlights (Teal & Orange)
    val cubeLutRawText: String = "",
    val generatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "speed_ramps",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class SpeedRampEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val clipId: String,
    val clipName: String,
    val rampType: String, // "Whip-Pan Snap (4x -> 0.25x)", "Slow-Mo Impact (0.2x)", "Hyper-Smooth Flow"
    val inSpeedMultiplier: Float = 1.0f,
    val peakSpeedMultiplier: Float = 3.5f,
    val impactSpeedMultiplier: Float = 0.25f,
    val outSpeedMultiplier: Float = 1.0f,
    val curveEasing: String = "Bezier EaseInOut",
    val opticalFlowEnabled: Boolean = true,
    val motionBlurStrength: Int = 75
)

@Entity(
    tableName = "subtitles",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class SubtitleItemEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val startTimecode: String,
    val endTimecode: String,
    val text: String,
    val language: String = "English",
    val stylePreset: String = "MrBeast Kinetic Pop", // "MrBeast Kinetic Pop", "Cinematic Clean", "Cyberpunk Neon", "Urdu Nastaliq Calligraphy"
    val highlightColor: String = "#FFDE59",
    val fontSizeSp: Int = 22
)

@Entity(
    tableName = "copilot_messages",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class CopilotMessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val isUser: Boolean,
    val messageText: String,
    val modelUsed: String = "gemini-3.1-pro-preview",
    val actionSuggested: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
