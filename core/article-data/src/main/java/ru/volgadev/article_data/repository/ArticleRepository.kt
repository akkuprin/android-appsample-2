package ru.volgadev.article_data.repository

import ru.volgadev.article_data.model.Article

interface ArticleRepository {

    suspend fun getArticles(pageNum: Int): List<Article>

    suspend fun getArticle(id: String): Article?
}