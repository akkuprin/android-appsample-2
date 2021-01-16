package ru.volgadev.article_data.model

import java.util.*

typealias StringPair = Pair<String, String>

data class Article(
    val id: String,
    val links: List<StringPair> = emptyList(),
    val symbol: String,
    val name: String,
    val descriptionHtml: String? = null,
    val iconUrl: String? = null,
    val tagline: String? = null,
    val currentPriceUsd: Double? = null
)

typealias PriceTimeSeries = ArrayList<Pair<Date, Double>>