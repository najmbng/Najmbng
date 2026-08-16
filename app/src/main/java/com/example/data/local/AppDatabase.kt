package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ClipEntity
import com.example.data.model.ColorGradeEntity
import com.example.data.model.CopilotMessageEntity
import com.example.data.model.EditorScriptEntity
import com.example.data.model.EdlItemEntity
import com.example.data.model.KeyframeAngleEntity
import com.example.data.model.MusicTrackEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SocialCopyEntity
import com.example.data.model.SpeedRampEntity
import com.example.data.model.SubtitleItemEntity

@Database(
    entities = [
        ProjectEntity::class,
        ClipEntity::class,
        KeyframeAngleEntity::class,
        EdlItemEntity::class,
        SocialCopyEntity::class,
        EditorScriptEntity::class,
        MusicTrackEntity::class,
        ColorGradeEntity::class,
        SpeedRampEntity::class,
        SubtitleItemEntity::class,
        CopilotMessageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun clipDao(): ClipDao
    abstract fun keyframeAngleDao(): KeyframeAngleDao
    abstract fun edlItemDao(): EdlItemDao
    abstract fun socialCopyDao(): SocialCopyDao
    abstract fun editorScriptDao(): EditorScriptDao
    abstract fun musicTrackDao(): MusicTrackDao
    abstract fun colorGradeDao(): ColorGradeDao
    abstract fun speedRampDao(): SpeedRampDao
    abstract fun subtitleDao(): SubtitleDao
    abstract fun copilotMessageDao(): CopilotMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "osmoflow_360_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
