package ru.volgadev.article_data.api

import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import ru.volgadev.article_data.BuildConfig
import ru.volgadev.article_data.model.Article
import ru.volgadev.article_data.model.StringPair
import ru.volgadev.common.BACKEND_URL
import ru.volgadev.common.log.Logger
import java.net.ConnectException

@WorkerThread
class ArticleBackendApiImpl : ArticleBackendApi {

    private val client = OkHttpClient()
    private val logger = Logger.get("ArticleBackendApiImpl")

    @Throws(ConnectException::class)
    override fun getArticlesOnPage(pageNum: Int): List<Article> {
        if (BuildConfig.DEBUG && pageNum < 0) {
            error("Page num must be non-negative")
        }
        val backendPageNum = pageNum + 1
        // TODO: понять почему падает в оффлайн
        val request: Request = Request.Builder().apply {
            url("$BACKEND_URL/assets?page=$backendPageNum")
        }.build()

        val result = arrayListOf<Article>()

        try {
            val response: Response = client.newCall(request).execute()
            val stringResponse = response.body?.string().orEmpty()
            logger.debug("response = $stringResponse")
            val json = JSONObject(stringResponse)
            val articlesArray = json.getJSONArray("data")
            for (i in 0 until articlesArray.length()) {
                val articleJson = articlesArray[i] as JSONObject
                val id = articleJson.optString("id")
                val symbol = articleJson.optString("symbol")
                val name = articleJson.optString("name")

                val overviewJson = articleJson.optJSONObject("profile")?.optJSONObject("general")
                    ?.optJSONObject("overview")

                val links = mutableListOf<StringPair>()

                overviewJson?.optJSONArray("official_links")?.let { tagsJs ->
                    for (t in 0 until tagsJs.length()) {
                        val a = tagsJs[t] as JSONObject
                        val linkName = a.optString("name")
                        val link = a.optString("link")
                        if (linkName.isNotBlank() && link.isNotBlank()) {
                            links.add(Pair(linkName, link))
                        }
                    }
                }

                var description: String? = null
                var tagline: String? = null

                overviewJson?.let { ojs ->
                    description =
                        if (!ojs.isNull("project_details")) ojs.optString("project_details") else null
                    tagline =
                        if (!ojs.isNull("tagline")) ojs.optString("tagline") else null
                }

                var priceUsd: Double? = null
                val marketData = articleJson.optJSONObject("metrics")?.optJSONObject("market_data")
                marketData?.let {
                    priceUsd = marketData.optDouble("price_usd")
                }

                result.add(
                    Article(
                        id = id,
                        name = name,
                        descriptionHtml = description,
                        links = links,
                        tagline = tagline,
                        symbol = symbol,
                        currentPriceUsd = priceUsd
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Error when get new articles $e")
            throw ConnectException("Error when get new articles $e")
        }
        logger.debug("All items: ${result.joinToString(", ")}}")
        return result
    }
}