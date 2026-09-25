package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_services")
data class SavedServiceEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val titleEn: String,
    val categoryName: String,
    val phone: String,
    val address: String,
    val upazila: String,
    val description: String,
    val timing: String,
    val rating: Float,
    val timestamp: Long = System.currentTimeMillis()
)
