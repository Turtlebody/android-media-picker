package com.turtlebody.imagepicker.core

import android.provider.MediaStore

/**
 * Created by WANGSUN on 27-Mar-19.
 */
object Constants {

    object Query{
        const val FOLDER_IMAGE = 501
    }

    object Projection{
        val FOLDER = arrayOf(
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA)

        val IMAGE = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Thumbnails.DATA)
    }

    object Intent{

    }

    object Fragment{
        val FOLDER_LIST = 101
        val IMAGE_LIST = 102
    }
}