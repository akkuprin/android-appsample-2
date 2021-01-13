package ru.volgadev.article_galery.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AnyThread
import androidx.cardview.widget.CardView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_galery.R
import ru.volgadev.common.log.Logger

private val ArticleDiffUtilCallback = object : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

class ArticleCardAdapter :
    PagedListAdapter<Article, ArticleCardAdapter.ViewHolder>(ArticleDiffUtilCallback) {

    private val logger = Logger.get("ArticleCardAdapter")

    inner class ViewHolder(private val card: CardView) : RecyclerView.ViewHolder(card) {
        private val textView = card.findViewById<TextView>(R.id.cardViewTitle)
        private val image = card.findViewById<ImageView>(R.id.cardViewImage)

        private val viewClickListener = View.OnClickListener { view ->
            view?.let {
                val id = view.tag as String
                logger.debug("On click $id")
                onItemClickListener?.onClick(id)
            }
        }

        fun bind(article: Article) {
            val holder = this
            card.tag = article.id
            val image = holder.image
            holder.textView.text = article.name

            Glide.with(image.context).load(article.iconUrl)
                .fallback(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(image)

            holder.card.setOnClickListener(viewClickListener)
        }
    }

    @Volatile
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(itemId: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_article, parent, false) as CardView

        return ViewHolder(card)
    }

    @AnyThread
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.bind(article)
        }
    }
}
