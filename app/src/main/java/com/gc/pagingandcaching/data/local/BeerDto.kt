package com.gc.pagingandcaching.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeerEntity(
   @PrimaryKey val id: Int,
    val name: String,
    val tagLine: String?,
    val description: String,
    val firstBrewed: String,
    val imageUrl: String?
)
