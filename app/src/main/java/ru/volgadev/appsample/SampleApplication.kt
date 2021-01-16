package ru.volgadev.appsample

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import ru.volgadev.article_data.data.ArticleBackendApiImpl
import ru.volgadev.article_data.data.ArticleRepositoryImpl
import ru.volgadev.article_data.domain.ArticleBackendApi
import ru.volgadev.article_data.domain.ArticleRepository
import ru.volgadev.article_galery.ui.ArticleGalleryViewModel
import ru.volgadev.article_page.ArticlePageViewModel
import ru.volgadev.common.log.AndroidLoggerDelegate
import ru.volgadev.common.log.Logger

class SampleApplication : Application() {

    private val sampleModule = module {
        single<ArticleRepository> {
            ArticleRepositoryImpl(
                articleBackendApi = get()
            )
        }
        single<ArticleBackendApi> { ArticleBackendApiImpl() }
        viewModel {
            ArticleGalleryViewModel(get())
        }
        viewModel {
            ArticlePageViewModel(get())
        }
    }

    override fun onCreate() {
        super.onCreate()

        Logger.setDelegates(AndroidLoggerDelegate())

        val koin = startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@SampleApplication)
            modules(sampleModule)
        }.koin
    }
}