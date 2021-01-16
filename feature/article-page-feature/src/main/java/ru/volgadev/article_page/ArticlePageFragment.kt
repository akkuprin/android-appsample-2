package ru.volgadev.article_page

import android.os.Bundle
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.view.View
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.layout_article_page.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.volgadev.common.log.Logger


const val ITEM_ID_KEY = "ITEM_ID"

class ArticlePageFragment : Fragment(R.layout.layout_article_page) {

    private val logger = Logger.get("ArticlePageFragment")

    private val viewModel: ArticlePageViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.debug("On fragment created")

        val args = arguments
        if (args != null && args.containsKey(ITEM_ID_KEY)) {
            val itemId = args.getString(ITEM_ID_KEY)
            if (itemId == null) {
                activity?.onBackPressed()
            } else {
                viewModel.onChooseArticle(itemId)
            }
        } else {
            throw IllegalStateException("You should set ITEM_ID_KEY in fragment attributes!")
        }

        backButton.setOnClickListener {
            logger.debug("On click back")
            activity?.onBackPressed()
        }

        viewModel.article.observe(viewLifecycleOwner, { article ->
            logger.debug("Set new ${article.id} article")
            toolbarText.text = article.name
            article.descriptionHtml?.let { descriptionHtml ->
                articleText.text = fromHtml(descriptionHtml, FROM_HTML_MODE_LEGACY)
            }
        })

        viewModel.articleTimeSeries.observe(viewLifecycleOwner, { timeSeries ->
            logger.debug("Show timeSeries ${timeSeries}")
            if (timeSeries != null) {
                val series = LineGraphSeries(
                    timeSeries.map { pair -> DataPoint(pair.first, pair.second) }.toTypedArray()
                )
                timeSeriesGraph.addSeries(series)
            }
        })
    }
}