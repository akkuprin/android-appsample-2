package ru.volgadev.article_data.repository

import androidx.annotation.WorkerThread
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.model.PriceTimeSeries

@WorkerThread
interface ArticleRepository {

    suspend fun getArticles(pageNum: Int): List<Article>

    suspend fun getArticle(id: String): Article?

    suspend fun getArticleLastMonthTimeSeries(id: String): PriceTimeSeries?
}