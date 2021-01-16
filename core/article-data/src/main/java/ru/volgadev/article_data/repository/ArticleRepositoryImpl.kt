package ru.volgadev.article_data.repository

import androidx.collection.ArrayMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.volgadev.article_data.api.ArticleBackendApi
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.model.PriceTimeSeries
import ru.volgadev.common.log.Logger
import java.util.*

private const val AVERAGE_DAY_IN_MONTH = 30

class ArticleRepositoryImpl(
    private val articleBackendApi: ArticleBackendApi
) : ArticleRepository {

    private val logger = Logger.get("ArticleRepositoryImpl")

    private val cashedArticles = ArrayMap<String, Article>()

    override suspend fun getArticles(pageNum: Int): List<Article> = withContext(Dispatchers.IO) {
        val newArticles = articleBackendApi.getArticlesOnPage(pageNum)
        newArticles.forEach { article ->
            cashedArticles[article.id] = article
        }
        return@withContext newArticles
    }

    override suspend fun getArticle(id: String): Article? = withContext(Dispatchers.Default) {
        logger.debug("getArticle($id)")
        return@withContext if (cashedArticles.contains(id)) cashedArticles[id] else null
    }

    override suspend fun getArticleLastMonthTimeSeries(id: String): PriceTimeSeries? =
        withContext(Dispatchers.IO) {
            logger.debug("getArticleLastMonthTimeSeries($id)")
            if (cashedArticles.contains(id)) {
                val startDate = Calendar.getInstance()
                    .apply { add(Calendar.DAY_OF_YEAR, -AVERAGE_DAY_IN_MONTH) }.time
                return@withContext articleBackendApi.getArticleTimeSeries(id, startDate = startDate)
            } else {
                return@withContext null
            }
        }

}