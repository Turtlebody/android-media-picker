package com.greentoad.turtlebody.mediapicker.core

import java.io.Serializable

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class MediaPickerConfig: Serializable {
    var mShowConfirmationDialog: Boolean = false
        private set
    var mAllowMultiSelection: Boolean = false
        private set
    var mUriPermanentAccess: Boolean = false
        private set
    var mScreenOrientation: Int? = null
        private set


    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }

    /**
     *  Show confirmation dialog after selecting file (works only for single file selection)
     */
    fun setShowConfirmationDialog(value: Boolean): MediaPickerConfig {
        mShowConfirmationDialog = value
        return this
    }

    /**
     * Allow multiple selection
     */
    fun setAllowMultiSelection(value: Boolean): MediaPickerConfig {
        mAllowMultiSelection = value
        return this
    }

    /**
     * Allow multiple selection
     */
    fun setUriPermanentAccess(value: Boolean): MediaPickerConfig {
        mUriPermanentAccess = value
        return this
    }


    fun setScreenOrientation(value: Int): MediaPickerConfig {
        mScreenOrientation = value
        return this
    }
}