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
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA)

        val AUDIO_FOLDER_NEW = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                "COUNT(" + MediaStore.Files.FileColumns.DATA + ") AS dataCount")

        val AUDIO_FILE = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION
        )
    }

    object Selection{

        val AUDIO_FOLDER = MediaStore.Files.FileColumns.MEDIA_TYPE+" = "+MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO+
        ") GROUP BY (" + MediaStore.Files.FileColumns.PARENT
    }

    object Intent{

    }

    object Fragment{
        val FOLDER_LIST = 101
        val IMAGE_LIST = 102
        val AUDIO_LIST = 103
    }
}