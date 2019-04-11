package com.turtlebody.mediapicker.core

import android.provider.MediaStore

/**
 * Created by WANGSUN on 27-Mar-19.
 */
object Constants {

    object FileTypes{
        const val FILE_TYPE_IMAGE = 501
        const val FILE_TYPE_VIDEO = 502
        const val FILE_TYPE_AUDIO = 503
    }

    object Projection{
        /**
         * Image Video projections
         */
        val IMAGE_VIDEO_FOLDER = arrayOf(
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA)

        val IMAGE_VIDEO_FILE = arrayOf(
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Thumbnails.DATA)

        /**
         * Audio projections
         */
        val AUDIO_FOLDER = arrayOf(
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.Media.DATA)

        val AUDIO_FILE = arrayOf(
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST
        )
    }

    object Intent{

    }

    object Fragment{
        val FOLDER_LIST = 101
        val IMAGE_LIST = 102
    }
}