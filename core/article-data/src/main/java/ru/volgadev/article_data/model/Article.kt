package ru.volgadev.article_data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias StringPair = Pair<String, String>

@Entity
data class Article(
    @PrimaryKey
    val id: String,
    val links: List<StringPair> = emptyList(),
    val symbol: String,
    val name: String,
    val descriptionHtml: String? = null,
    val iconUrl: String? = null,
    val tagline: String? = null
)