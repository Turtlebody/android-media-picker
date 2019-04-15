package com.greentoad.turtlebody.mediapicker.core

import java.io.Serializable

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class PickerConfig: Serializable {
    var mShowDialog: Boolean = false
    var mAllowMultiImages: Boolean = false

    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }

    /**
     *  Show dialog after selecting file
     */
    fun setShowDialog(value: Boolean): PickerConfig {
        mShowDialog = value
        return this
    }

    /**
     * Allow multiple selection
     */
    fun setAllowMultiImages(value: Boolean): PickerConfig {
        mAllowMultiImages = value
        return this
    }
}