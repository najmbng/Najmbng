package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ClipEntity
import com.example.data.model.EditorScriptEntity
import com.example.data.model.EdlItemEntity
import com.example.data.model.KeyframeAngleEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SocialCopyEntity

@Database(
    entities = [
        ProjectEntity::class,
        ClipEntity::class,
        KeyframeAngleEntity::class,
        EdlItemEntity::class,
        SocialCopyEntity::class,
        EditorScriptEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun clipDao(): ClipDao
    abstract fun keyframeAngleDao(): KeyframeAngleDao
    abstract fun edlItemDao(): EdlItemDao
    abstract fun socialCopyDao(): SocialCopyDao
    abstract fun editorScriptDao(): EditorScriptDao

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
