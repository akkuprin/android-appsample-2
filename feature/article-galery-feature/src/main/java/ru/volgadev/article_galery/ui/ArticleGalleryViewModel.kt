package ru.volgadev.article_galery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.repository.ArticleRepository
import ru.volgadev.common.log.Logger

internal const val MAX_ITEMS_COUNT_ON_PAGE = 20

class ArticleGalleryViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

//    val _articles = MutableLiveData<HashMap<Int, ArrayList<Article>>>()
//    val articles: LiveData<HashMap<Int, ArrayList<Article>>> = _articles

//    fun onNewPage(pageNum: Int) {
//        viewModelScope.launch {
//            val pageArticles = articleRepository.getArticles(pageNum)
//        }
//    }

    fun getPositionalDataSource(): PositionalDataSource<Article> = ArticleDataSource(viewModelScope, articleRepository)

    internal class ArticleDataSource(private val coroutineScope: CoroutineScope, private val articleRepository: ArticleRepository) : PositionalDataSource<Article>() {

        private val logger = Logger.get("ArticleDataSource")

        // TODO: make correct pagination

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Article>) {
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
            val loadPage = params.startPosition/MAX_ITEMS_COUNT_ON_PAGE
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