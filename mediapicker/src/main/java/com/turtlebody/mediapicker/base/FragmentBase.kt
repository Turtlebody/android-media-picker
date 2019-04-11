package com.turtlebody.mediapicker.base

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger

abstract class FragmentBase : Fragment(),AnkoLogger, Toolbar.OnMenuItemClickListener {
    private var toolbar: Toolbar? = null


    var toolbarTitle: String = ""
        set(value) {
            toolbar?.title = value
            field = value
        }

    //protected abstract String getToolBarDefaultTitle();

    fun initToolbar(toolbar: Toolbar?, navigationDrawable: Int, isBackNavigationEnable: Boolean) {

        this.toolbar = toolbar
        toolbar?.setNavigationIcon(navigationDrawable)
        toolbar?.setOnMenuItemClickListener(this)
        if (isBackNavigationEnable) {
            toolbar?.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return false
    }
}