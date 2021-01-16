package ru.volgadev.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import java.text.SimpleDateFormat
import java.util.*

fun Context.applicationDataDir(): String {
    val p: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
    return p.applicationInfo.dataDir + "/"
}

fun Drawable.toBitmap(): Bitmap {
    val drawable = this
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Activity.hideNavBar() {
    window.hideNavBar()
}

fun Activity.showNavBar() {
    window.showNavBar()
}

fun Window.hideNavBar() {
    val decorView: View = this.decorView
    val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    decorView.systemUiVisibility = uiOptions
}

fun Window.showNavBar() {
    val decorView: View = this.decorView
    val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    decorView.systemUiVisibility = uiOptions
}

fun View.setVisibleWithTransition(
    visibility: Int,
    transition: Transition,
    durationMs: Long,
    parent: ViewGroup,
    delayMs: Long = 0L
) {
    transition.duration = durationMs
    transition.startDelay = delayMs
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(parent, transition)
    this.visibility = visibility
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
