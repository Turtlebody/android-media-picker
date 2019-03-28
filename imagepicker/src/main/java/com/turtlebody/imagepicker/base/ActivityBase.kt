package com.turtlebody.imagepicker.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar.*
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
//            toolbar.title = value
        }

    fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }


    fun initToolbar(resId: Int, toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setToolbarNavigationIcon(resId, toolbar)
    }


    fun setToolbarNavigationIcon(resId: Int, toolbar: Toolbar) {
        toolbar.setNavigationIcon(resId)
    }
}
