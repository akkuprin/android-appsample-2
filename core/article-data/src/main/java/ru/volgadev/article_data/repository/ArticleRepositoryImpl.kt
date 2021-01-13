package ru.volgadev.article_data.repository

import android.content.Context
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

    override suspend fun getArticles(page: Int): List<Article> = withContext(Dispatchers.IO) {
        val newArticles = articleBackendApi.get(1)
        return@withContext newArticles
    }

    // TODO: make local cashe
    override suspend fun getArticle(id: String): Article? = withContext(Dispatchers.Default) {
        logger.debug("Get article with id $id")
        return@withContext null
    }

}