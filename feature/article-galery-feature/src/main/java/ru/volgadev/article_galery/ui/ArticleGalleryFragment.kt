package ru.volgadev.article_galery.ui

import android.os.Bundle
import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import androidx.annotation.AnyThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.galery_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.volgadev.article_galery.R
import ru.volgadev.common.log.Logger

class ArticleGalleryFragment : Fragment(R.layout.galery_fragment) {

    private val logger = Logger.get("ArticleGalleryFragment")

    private val viewModel: ArticleGaleryViewModel by viewModel()

    // TODO: another way to send data from fragment to activity
    interface OnItemClickListener {
        fun onClick(itemId: String)
    }

    @Volatile
    private var onItemClickListener: OnItemClickListener? = null

    @AnyThread
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.debug("On fragment created")

        val viewAdapter = ArticleCardAdapter().apply {
            setOnItemClickListener(object : ArticleCardAdapter.OnItemClickListener {
                override fun onClick(itemId: String) {
                    onItemClickListener?.onClick(itemId)
                }
            })
        }

        contentRecyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = viewAdapter
            overScrollMode = OVER_SCROLL_NEVER
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 248
                removeDuration = 200
                moveDuration = 200
                changeDuration = 0
            }
        }

        viewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            logger.debug("Set new ${articles.size} articles")
            viewAdapter.setDataset(articles)
        })
    }

}