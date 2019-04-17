package com.greentoad.turtlebody.mediapicker.core

import java.io.Serializable

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class PickerConfig: Serializable {
    var mShowConfirmationDialog: Boolean = false
    var mAllowMultiImages: Boolean = false

    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }

    /**
     *  Show confirmation dialog after selecting file (works only for single file selection)
     */
    fun setShowConfirmationDialog(value: Boolean): PickerConfig {
        mShowConfirmationDialog = value
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