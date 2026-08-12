package sk.mkdigital.androidshowcase.presentation.base.router

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

interface LocaleRouter {
    fun setLocale(tag: String)

    class Impl : LocaleRouter {
        override fun setLocale(tag: String) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        }
    }
}
