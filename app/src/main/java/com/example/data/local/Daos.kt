package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.ClipEntity
import com.example.data.model.EditorScriptEntity
import com.example.data.model.EdlItemEntity
import com.example.data.model.KeyframeAngleEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SocialCopyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: String): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectDirect(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)
}

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getClipsForProject(projectId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE id = :clipId")
    fun getClipById(clipId: String): Flow<ClipEntity?>

    @Query("SELECT * FROM clips WHERE projectId = :projectId ORDER BY reelsScore DESC")
    fun getClipsRankedByReels(projectId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE projectId = :projectId ORDER BY landscapeScore DESC")
    fun getClipsRankedByLandscape(projectId: String): Flow<List<ClipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClips(clips: List<ClipEntity>)

    @Update
    suspend fun updateClip(clip: ClipEntity)

    @Query("DELETE FROM clips WHERE id = :clipId")
    suspend fun deleteClipById(clipId: String)
}

@Dao
interface KeyframeAngleDao {
    @Query("SELECT * FROM keyframe_angles WHERE clipId = :clipId ORDER BY timestampSeconds ASC")
    fun getKeyframesForClip(clipId: String): Flow<List<KeyframeAngleEntity>>

    @Query("SELECT * FROM keyframe_angles WHERE clipId = :clipId ORDER BY timestampSeconds ASC")
    suspend fun getKeyframesForClipDirect(clipId: String): List<KeyframeAngleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyframes(keyframes: List<KeyframeAngleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyframe(keyframe: KeyframeAngleEntity)

    @Query("DELETE FROM keyframe_angles WHERE clipId = :clipId")
    suspend fun deleteKeyframesForClip(clipId: String)
}

@Dao
interface EdlItemDao {
    @Query("SELECT * FROM edl_items WHERE projectId = :projectId ORDER BY sequenceOrder ASC")
    fun getEdlItemsForProject(projectId: String): Flow<List<EdlItemEntity>>

    @Query("SELECT * FROM edl_items WHERE projectId = :projectId ORDER BY sequenceOrder ASC")
    suspend fun getEdlItemsDirect(projectId: String): List<EdlItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdlItems(items: List<EdlItemEntity>)

    @Query("DELETE FROM edl_items WHERE projectId = :projectId")
    suspend fun deleteEdlItemsForProject(projectId: String)
}

@Dao
interface SocialCopyDao {
    @Query("SELECT * FROM social_copies WHERE projectId = :projectId")
    fun getSocialCopiesForProject(projectId: String): Flow<List<SocialCopyEntity>>

    @Query("SELECT * FROM social_copies WHERE projectId = :projectId AND language = :language LIMIT 1")
    fun getSocialCopyByLanguage(projectId: String, language: String): Flow<SocialCopyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialCopies(copies: List<SocialCopyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialCopy(copy: SocialCopyEntity)

    @Query("DELETE FROM social_copies WHERE projectId = :projectId")
    suspend fun deleteSocialCopiesForProject(projectId: String)
}

@Dao
interface EditorScriptDao {
    @Query("SELECT * FROM editor_scripts WHERE projectId = :projectId ORDER BY generatedAt DESC LIMIT 1")
    fun getLatestScriptForProject(projectId: String): Flow<EditorScriptEntity?>

    @Query("SELECT * FROM editor_scripts WHERE projectId = :projectId ORDER BY generatedAt DESC LIMIT 1")
    suspend fun getLatestScriptDirect(projectId: String): EditorScriptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorScript(script: EditorScriptEntity)

    @Query("DELETE FROM editor_scripts WHERE projectId = :projectId")
    suspend fun deleteScriptsForProject(projectId: String)
}

@Dao
interface MusicTrackDao {
    @Query("SELECT * FROM music_tracks WHERE projectId = :projectId ORDER BY generatedAt DESC")
    fun getMusicTracksForProject(projectId: String): Flow<List<com.example.data.model.MusicTrackEntity>>

    @Query("SELECT * FROM music_tracks WHERE projectId = :projectId ORDER BY generatedAt DESC LIMIT 1")
    fun getActiveMusicTrack(projectId: String): Flow<com.example.data.model.MusicTrackEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusicTrack(track: com.example.data.model.MusicTrackEntity)

    @Query("DELETE FROM music_tracks WHERE projectId = :projectId")
    suspend fun deleteMusicTracksForProject(projectId: String)
}

@Dao
interface ColorGradeDao {
    @Query("SELECT * FROM color_grades WHERE projectId = :projectId ORDER BY generatedAt DESC LIMIT 1")
    fun getActiveColorGrade(projectId: String): Flow<com.example.data.model.ColorGradeEntity?>

    @Query("SELECT * FROM color_grades WHERE projectId = :projectId ORDER BY generatedAt DESC")
    fun getAllColorGradesForProject(projectId: String): Flow<List<com.example.data.model.ColorGradeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColorGrade(grade: com.example.data.model.ColorGradeEntity)

    @Query("DELETE FROM color_grades WHERE projectId = :projectId")
    suspend fun deleteColorGradesForProject(projectId: String)
}

@Dao
interface SpeedRampDao {
    @Query("SELECT * FROM speed_ramps WHERE projectId = :projectId")
    fun getSpeedRampsForProject(projectId: String): Flow<List<com.example.data.model.SpeedRampEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeedRamps(ramps: List<com.example.data.model.SpeedRampEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeedRamp(ramp: com.example.data.model.SpeedRampEntity)

    @Query("DELETE FROM speed_ramps WHERE projectId = :projectId")
    suspend fun deleteSpeedRampsForProject(projectId: String)
}

@Dao
interface SubtitleDao {
    @Query("SELECT * FROM subtitles WHERE projectId = :projectId ORDER BY startTimecode ASC")
    fun getSubtitlesForProject(projectId: String): Flow<List<com.example.data.model.SubtitleItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtitles(subs: List<com.example.data.model.SubtitleItemEntity>)

    @Query("DELETE FROM subtitles WHERE projectId = :projectId")
    suspend fun deleteSubtitlesForProject(projectId: String)
}

@Dao
interface CopilotMessageDao {
    @Query("SELECT * FROM copilot_messages WHERE projectId = :projectId ORDER BY timestamp ASC")
    fun getMessagesForProject(projectId: String): Flow<List<com.example.data.model.CopilotMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: com.example.data.model.CopilotMessageEntity)

    @Query("DELETE FROM copilot_messages WHERE projectId = :projectId")
    suspend fun deleteMessagesForProject(projectId: String)
}
