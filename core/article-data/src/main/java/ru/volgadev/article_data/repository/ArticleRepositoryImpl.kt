package ru.volgadev.article_data.repository

import android.content.Context
import androidx.collection.ArrayMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.volgadev.article_data.api.ArticleBackendApi
import ru.volgadev.article_data.model.Article
import ru.volgadev.common.log.Logger

class ArticleRepositoryImpl(
    private val context: Context,
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
        logger.debug("Get article with id $id")
        val article = if (cashedArticles.contains(id)) cashedArticles[id] else null
        return@withContext article
    }

}