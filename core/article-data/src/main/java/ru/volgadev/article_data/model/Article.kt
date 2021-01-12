package ru.volgadev.article_data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey
    val id: String,
    val links: List<String> = emptyList(),
    val symbol: String,
    val name: String,
    val description: String,
    val iconUrl: String? = null,
    val tagline: String? = null
)