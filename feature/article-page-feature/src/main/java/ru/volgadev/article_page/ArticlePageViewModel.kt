package ru.volgadev.article_page

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.model.PriceTimeSeries
import ru.volgadev.article_data.repository.ArticleRepository
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
            val article = repository.getArticle(id)
            if (article!=null) {
                logger.debug("Use article ${article.id}")
                _article.postValue(article)

                val timeSeries = repository.getArticleLastMonthTimeSeries(article.id)
                _articleTimeSeries.postValue(timeSeries)
            } else {
                logger.error("Article $id not found")
            }
        }
    }
}