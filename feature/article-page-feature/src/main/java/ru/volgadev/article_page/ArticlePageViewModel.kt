package ru.volgadev.article_page

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.volgadev.article_data.domain.Article
import ru.volgadev.article_data.domain.ArticleRepository
import ru.volgadev.article_data.domain.PriceTimeSeries
import ru.volgadev.common.ErrorResult
import ru.volgadev.common.SuccessResult
import ru.volgadev.common.log.Logger

class ArticlePageViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val logger = Logger.get("ArticlePageViewModel")

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    private val _articleTimeSeries = MutableLiveData<PriceTimeSeries?>()
    val articleTimeSeries: LiveData<PriceTimeSeries?> = _articleTimeSeries

    @AnyThread
    fun onChooseArticle(id: String) {
        viewModelScope.launch {
            val articleResult = repository.getArticle(id)
            when (articleResult){
                is SuccessResult -> {
                    val article = articleResult.data
                    logger.debug("Use article ${article.id}")
                    _article.postValue(article)

                    val timeSeriesResult = repository.getArticleLastMonthTimeSeries(article.id)
                    when (timeSeriesResult){
                        is SuccessResult -> {
                            _articleTimeSeries.postValue(timeSeriesResult.data)
                        }
                        is ErrorResult -> {
                            logger.error("Error when load time series")
                        }
                    }
                }
                is ErrorResult -> {
                    logger.error("Article $id not found")
                }
            }
        }
    }
}