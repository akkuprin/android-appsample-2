package ru.volgadev.article_galery.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.repository.ArticleRepository
import ru.volgadev.common.log.Logger
import java.util.concurrent.Executors

internal const val MAX_ITEMS_COUNT_ON_PAGE = 20

class ArticleGalleryViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<PagedList<Article>>()
    val articlesPagedList: LiveData<PagedList<Article>> = _articles

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(MAX_ITEMS_COUNT_ON_PAGE)
                .setPageSize(MAX_ITEMS_COUNT_ON_PAGE)
                .build()

            val dataSource = ArticleDataSource(viewModelScope, articleRepository)

            val mainHandler = Handler(Looper.getMainLooper())

            val pagedList: PagedList<Article> =
                PagedList.Builder(dataSource, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor { mainHandler.post(it) }
                    .build()
            _articles.postValue(pagedList)
        }
    }

    internal class ArticleDataSource(
        private val coroutineScope: CoroutineScope,
        private val articleRepository: ArticleRepository
    ) : PositionalDataSource<Article>() {

        private val logger = Logger.get("ArticleDataSource")

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<Article>
        ) {
            logger.debug(
                "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                        ", requestedLoadSize = " + params.requestedLoadSize + ", pageSize = " + params.pageSize
            )
            coroutineScope.launch {
                val pageArticles = articleRepository.getArticles(0)
                callback.onResult(pageArticles, params.requestedStartPosition)
            }
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Article>) {
            val loadPage = params.startPosition / MAX_ITEMS_COUNT_ON_PAGE
            logger.debug(
                "loadRange. startPosition = ${params.startPosition}; loadPage = $loadPage"
            )
            coroutineScope.launch {
                val pageArticles = articleRepository.getArticles(loadPage)
                callback.onResult(pageArticles)
            }
        }
    }
}