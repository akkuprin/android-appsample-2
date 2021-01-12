package ru.volgadev.article_data.api

import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import ru.volgadev.article_data.model.Article
import ru.volgadev.common.BACKEND_URL
import ru.volgadev.common.log.Logger
import java.net.ConnectException


@WorkerThread
class ArticleBackendApiImpl : ArticleBackendApi {

    private val client = OkHttpClient()
    private val logger = Logger.get("ArticleBackendApiImpl")

    @Throws(ConnectException::class)
    override fun getUpdates(lastUpdateTime: Long): List<Article> {
        val request: Request = Request.Builder().apply {
            url("$BACKEND_URL/api/v2/assets?with-metrics&page=1")
        }.build()

        val result = arrayListOf<Article>()

        try {
            val response: Response = client.newCall(request).execute()
            val stringResponse = response.body!!.string()
            logger.debug("response = $stringResponse")
            val json = JSONObject(stringResponse)
            val articlesArray = json.getJSONArray("data")
            for (i in 0 until articlesArray.length()) {
                val articleJson = articlesArray[i] as JSONObject
                val id = articleJson.optString("id")
                val symbol = articleJson.optString("symbol")
                val name = articleJson.optString("name")

                val generalJson = articleJson.optJSONObject("profile")?.optJSONObject("general")

                val links = arrayListOf<String>()

                generalJson?.optJSONArray("official_links")?.let { tagsJs ->
                    for (t in 0 until tagsJs.length()) {
                        links.add(tagsJs[t].toString())
                    }
                }

                val description = generalJson?.optString("project_details").orEmpty()
                val tagline = generalJson?.optString("tagline").orEmpty()

                result.add(
                    Article(
                        id = id,
                        name = name,
                        description = description,
                        links = links,
                        tagline = tagline,
                        symbol = symbol
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Error when get new articles $e")
            throw ConnectException("Error when get new articles $e")
        }
        return result
    }
}