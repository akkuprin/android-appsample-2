package ru.volgadev.article_data.domain

import androidx.annotation.WorkerThread
import ru.volgadev.common.DataResult

@WorkerThread
interface ArticleRepository {

    suspend fun getArticles(pageNum: Int): DataResult<List<Article>>

    suspend fun getArticle(id: String): DataResult<Article>

    suspend fun getArticleLastMonthTimeSeries(id: String): DataResult<PriceTimeSeries>
}