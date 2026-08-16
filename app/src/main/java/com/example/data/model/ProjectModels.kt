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
