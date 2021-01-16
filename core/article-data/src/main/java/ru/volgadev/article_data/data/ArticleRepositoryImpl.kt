package ru.volgadev.article_data.data

import androidx.collection.ArrayMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.volgadev.article_data.domain.Article
import ru.volgadev.article_data.domain.ArticleBackendApi
import ru.volgadev.article_data.domain.ArticleRepository
import ru.volgadev.article_data.domain.PriceTimeSeries
import ru.volgadev.common.DataResult
import ru.volgadev.common.ErrorResult
import ru.volgadev.common.SuccessResult
import ru.volgadev.common.log.Logger
import java.net.ConnectException
import java.util.*

private const val AVERAGE_DAY_IN_MONTH = 30

class ArticleRepositoryImpl(
    private val articleBackendApi: ArticleBackendApi
) : ArticleRepository {

    private val logger = Logger.get("ArticleRepositoryImpl")

    private val cashedArticles = ArrayMap<String, Article>()

    override suspend fun getArticles(pageNum: Int): DataResult<List<Article>> =
        withContext(Dispatchers.IO) {
            try {
                val newArticles = articleBackendApi.getArticlesOnPage(pageNum)
                newArticles.forEach { article ->
                    cashedArticles[article.id] = article
                }
                return@withContext SuccessResult(newArticles)
            } catch (e: ConnectException) {
                return@withContext ErrorResult(e.message, e)
            }
        }

    override suspend fun getArticle(id: String): DataResult<Article> =
        withContext(Dispatchers.Default) {
            logger.debug("getArticle($id)")
            return@withContext if (cashedArticles.contains(id)) {
                SuccessResult(cashedArticles[id]!!)
            } else {
                ErrorResult("No item with id $id")
            }
        }

    override suspend fun getArticleLastMonthTimeSeries(id: String): DataResult<PriceTimeSeries> =
        withContext(Dispatchers.IO) {
            logger.debug("getArticleLastMonthTimeSeries($id)")
            if (cashedArticles.contains(id)) {
                try {
                    val startDate = Calendar.getInstance()
                        .apply { add(Calendar.DAY_OF_YEAR, -AVERAGE_DAY_IN_MONTH) }.time
                    return@withContext SuccessResult(
                        articleBackendApi.getArticleTimeSeries(
                            id,
                            startDate = startDate
                        )
                    )
                } catch (e: ConnectException) {
                    return@withContext ErrorResult(e.message, e)
                }
            } else {
                return@withContext ErrorResult("No item with id $id")
            }
        }
}