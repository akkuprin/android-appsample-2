package ru.volgadev.article_data.api

import androidx.annotation.WorkerThread
import ru.volgadev.article_data.model.Article

@WorkerThread
interface ArticleBackendApi {
    fun getArticlesOnPage(pageNum: Int): List<Article>
}