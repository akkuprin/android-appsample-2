package ru.volgadev.appsample.ui

import androidx.fragment.app.Fragment
import ru.volgadev.article_galery.ui.ArticleGalleryFragment
import ru.volgadev.article_page.ArticlePageFragment

enum class AppFragment {
    GALERY, ARTICLE_PAGE
}

object FragmentProvider {

    private val FULLSCREEN_FRAGMENTS_CLASS_NAMES =
        listOf(ArticlePageFragment::class.java.name)

    fun create(code: AppFragment): Fragment {
        return when (code) {
            AppFragment.GALERY -> {
                ArticleGalleryFragment()
            }
            AppFragment.ARTICLE_PAGE -> {
                ArticlePageFragment()
            }
        }
    }

    fun isFullscreen(fragment: Fragment): Boolean {
        return fragment::class.java.name in FULLSCREEN_FRAGMENTS_CLASS_NAMES
    }
}