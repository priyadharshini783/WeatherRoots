package com.example.weatherroots.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        VoiceMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherRootsDatabase :
    RoomDatabase() {

    abstract fun voiceMessageDao():
            VoiceMessageDao

    companion object {

        @Volatile
        private var INSTANCE:
                WeatherRootsDatabase? = null

        fun getDatabase(
            context: Context
        ): WeatherRootsDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            WeatherRootsDatabase::class.java,
                            "weatherroots_database"
                        )
                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}