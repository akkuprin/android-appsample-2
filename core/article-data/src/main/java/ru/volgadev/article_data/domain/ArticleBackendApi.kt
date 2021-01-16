package ru.volgadev.article_data.domain

import androidx.annotation.WorkerThread
import java.util.Date
import java.util.Calendar

@WorkerThread
interface ArticleBackendApi {
    fun getArticlesOnPage(pageNum: Int): List<Article>

    fun getArticleTimeSeries(
        articleId: String,
        startDate: Date,
        endDate: Date = Calendar.getInstance().time
    ): PriceTimeSeries
}