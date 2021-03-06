package ru.volgadev.appsample.ui

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*
import ru.volgadev.appsample.R
import ru.volgadev.article_galery.ui.ArticleGalleryFragment
import ru.volgadev.article_page.ArticlePageFragment
import ru.volgadev.article_page.ITEM_ID_KEY
import ru.volgadev.common.hideNavBar
import ru.volgadev.common.log.Logger
import ru.volgadev.common.setVisibleWithTransition

const val HOME_ITEM_ID = R.id.action_home
const val GALERY_ITEM_ID = R.id.action_galery

class MainActivity : AppCompatActivity() {

    private val logger = Logger.get("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.debug("On create")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val galleryFragment = FragmentProvider.create(AppFragment.GALERY) as ArticleGalleryFragment
        galleryFragment.setOnItemClickListener(object : ArticleGalleryFragment.OnItemClickListener {
            override fun onClick(itemId: String) {
                logger.debug("Choose $itemId item to show")
                val itemPageFragment = FragmentProvider.create(AppFragment.ARTICLE_PAGE) as ArticlePageFragment
                showFragment(
                    itemPageFragment,
                    Bundle().apply { putString(ITEM_ID_KEY, itemId) },
                    true
                )
            }
        })

        bottomNavigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    HOME_ITEM_ID -> {
                        Toast.makeText(
                            applicationContext,
                            "TODO: On click home",
                            Toast.LENGTH_SHORT
                        ).show()
                        return true
                    }
                    GALERY_ITEM_ID -> {
                        showFragment(galleryFragment)
                        return true
                    }
                }
                return false
            }
        })
        bottomNavigation.selectedItemId = GALERY_ITEM_ID

        supportFragmentManager.addOnBackStackChangedListener {
            supportFragmentManager.findFragmentById(
                R.id.contentContainer
            )?.let { provideNavigationPanelVisibility(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        logger.debug("onResume()")
        hideNavBar()
    }

    private fun showFragment(
        fragment: Fragment,
        arguments: Bundle? = null,
        addToBackStack: Boolean = false
    ) {
        logger.debug("Show fragment ${fragment.javaClass.canonicalName}")
        fragment.arguments = arguments
        val transaction = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.contentContainer, fragment)

        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun provideNavigationPanelVisibility(fragment: Fragment) {
        if (FragmentProvider.isFullscreen(fragment)) {
            hideBottomNavigationPanel()
        } else {
            showBottomNavigationPanel()
        }
    }

    private fun showBottomNavigationPanel() {
        logger.debug("showBottomNavigationPanel()")
        bottomNavigation.setVisibleWithTransition(
            View.VISIBLE,
            Slide(Gravity.BOTTOM), 600, mainActivityLayout
        )
    }

    private fun hideBottomNavigationPanel() {
        logger.debug("hideBottomNavigationPanel()")
        bottomNavigation.setVisibleWithTransition(
            View.GONE,
            Slide(Gravity.BOTTOM), 600, mainActivityLayout
        )
    }
}