package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedServiceDao {
    @Query("SELECT * FROM saved_services ORDER BY timestamp DESC")
    fun getAllSavedServices(): Flow<List<SavedServiceEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_services WHERE id = :id)")
    fun isServiceSaved(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveService(service: SavedServiceEntity)

    @Query("DELETE FROM saved_services WHERE id = :id")
    suspend fun removeSavedService(id: String)

    @Query("SELECT COUNT(*) FROM saved_services")
    fun getSavedCount(): Flow<Int>
}
