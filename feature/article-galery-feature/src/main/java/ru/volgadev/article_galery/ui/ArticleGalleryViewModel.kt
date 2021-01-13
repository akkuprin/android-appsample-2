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
                        ", requestedLoadSize = " + params.requestedLoadSize
            )
            coroutineScope.launch {
                val pageArticles = articleRepository.getArticles(1)
                callback.onResult(pageArticles, 0)
            }
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Article>) {
            logger.debug(
                "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize
            )
            coroutineScope.launch {
                val pageArticles = articleRepository.getArticles(2)
                callback.onResult(pageArticles)
            }
        }
    }
}