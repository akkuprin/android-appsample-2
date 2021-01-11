package ru.volgadev.article_galery.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AnyThread
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_galery.R
import ru.volgadev.common.log.Logger

class ArticleCardAdapter :
    RecyclerView.Adapter<ArticleCardAdapter.ViewHolder>() {

    private val logger = Logger.get("ArticleCardAdapter")

    inner class ViewHolder(val card: CardView) : RecyclerView.ViewHolder(card) {
        val textView = card.findViewById<TextView>(R.id.cardViewTitle)
        val image = card.findViewById<ImageView>(R.id.cardViewImage)

        private val viewClickListener = View.OnClickListener { view ->
            view?.let {
                val id = view.tag as Long
                logger.debug("On click $id")
                onItemClickListener?.onClick(id)
            }
        }

        fun bind(article: Article) {
            val holder = this
            card.tag = article.id
            val image = holder.image
            holder.textView.text = article.title

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
        fun onClick(itemId: Long)
    }

    private var articleList = ArrayList<Article>()

    @AnyThread
    fun setDataset(dataset: Collection<Article>) {
        logger.debug("Set dataset with ${dataset.size} members")

        if (articleList.isNotEmpty()) {
            val length = articleList.size
            articleList.clear()
            notifyItemRangeRemoved(0, length);
        }
        articleList = ArrayList(dataset)
        notifyItemRangeInserted(0, articleList.size)
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
        val article = articleList[position]
        holder.bind(article)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = articleList.size
}
