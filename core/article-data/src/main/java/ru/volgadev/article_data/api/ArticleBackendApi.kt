package ru.volgadev.article_data.api

import androidx.annotation.WorkerThread
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.model.PriceTimeSeries
import java.util.*

@WorkerThread
interface ArticleBackendApi {
    fun getArticlesOnPage(pageNum: Int): List<Article>

    fun getArticleTimeSeries(
        articleId: String,
        startDate: Date,
        endDate: Date = Calendar.getInstance().time
    ): PriceTimeSeries
}