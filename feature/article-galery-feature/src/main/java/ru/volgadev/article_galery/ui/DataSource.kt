package ru.volgadev.article_galery.ui

import androidx.paging.PositionalDataSource
import ru.volgadev.article_data.model.Article
import ru.volgadev.common.log.Logger

internal class ArticleDataSource : PositionalDataSource<Article>() {

    private val logger = Logger.get("ArticleDataSource")

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Article>) {
        logger.debug(
            "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                    ", requestedLoadSize = " + params.requestedLoadSize
        )

        callback.onResult(emptyList(), 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Article>) {
        logger.debug(
            "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize
        )

        callback.onResult(emptyList())
    }
}