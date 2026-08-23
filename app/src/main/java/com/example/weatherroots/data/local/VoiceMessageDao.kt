package com.example.weatherroots.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VoiceMessageDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertMessage(
        message: VoiceMessageEntity
    )

    @Query(
        """
        SELECT * FROM voice_messages
        ORDER BY timestamp ASC, id ASC
        """
    )
    fun getAllMessages():
            Flow<List<VoiceMessageEntity>>

    @Query(
        "DELETE FROM voice_messages"
    )
    suspend fun clearAllMessages()
}