package com.greentoad.turtlebody.mediapicker.ui.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.greentoad.turtlebody.mediapicker.R
import org.jetbrains.anko.AnkoLogger


abstract class ActivityBase : AppCompatActivity(), AnkoLogger {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    var toolbarTitle: String
        get() = supportActionBar?.title.toString()
        set(value) {
            val actionBar = supportActionBar
            actionBar?.title = value
        }

    fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }


    fun initToolbar(resId: Int, toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setToolbarNavigationIcon(resId, toolbar)
    }


    private fun setToolbarNavigationIcon(resId: Int, toolbar: Toolbar) {
        toolbar.setNavigationIcon(resId)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.md_white_1000)
        }
       setLightStatusBar(window.decorView, this)

    }

    private fun setLightStatusBar(view: View, activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = Color.WHITE
        }
    }
}
