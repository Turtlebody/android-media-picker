package com.greentoad.turtlebody.mediapicker.core

import java.io.Serializable

/**
 * Created by WANGSUN on 29-Mar-19.
 */
class ImagePickerConfig: Serializable {
    var mShowConfirmationDialog: Boolean = false
    var mAllowMultiImages: Boolean = false
    var mUriPermanentAccess: Boolean = false


    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }

    /**
     *  Show confirmation dialog after selecting file (works only for single file selection)
     */
    fun setShowConfirmationDialog(value: Boolean): ImagePickerConfig {
        mShowConfirmationDialog = value
        return this
    }

    /**
     * Allow multiple selection
     */
    fun setAllowMultiImages(value: Boolean): ImagePickerConfig {
        mAllowMultiImages = value
        return this
    }

    /**
     * Allow multiple selection
     */
    fun setUriPermanentAccess(value: Boolean): ImagePickerConfig {
        mUriPermanentAccess = value
        return this
    }
}