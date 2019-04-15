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

    object Queries{
        val imageQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        //private val audioQueryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val audioQueryUri = MediaStore.Files.getContentUri("external")
    }

    object Projection{
        /**
         * Image projections
         */
        val IMAGE_FOLDER = arrayOf(
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA)

        val IMAGE_FILE = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Thumbnails.DATA)

        /**
         * Video projections
         */
        val VIDEO_FOLDER = arrayOf(
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA)

        val VIDEO_FILE = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Media.DURATION)
        /**
         * Audio projections
         */
        @Deprecated("This only works for album")
        val ALBUM_FOLDER = arrayOf(
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA)

        val AUDIO_FOLDER = arrayOf(
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
        val IMAGE_VIDEO_FOLDER = 101
        val AUDIO_FOLDER = 102

        val IMAGE_LIST = 103
        val VIDEO_LIST = 104
        val AUDIO_LIST = 105
    }
}