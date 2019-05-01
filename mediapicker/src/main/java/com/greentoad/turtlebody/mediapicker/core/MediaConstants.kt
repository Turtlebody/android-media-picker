package com.greentoad.turtlebody.mediapicker.core

import android.provider.MediaStore

/**
 * Created by WANGSUN on 27-Mar-19.
 */
object MediaConstants {

    object Queries{
        val imageQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val audioQueryUri = MediaStore.Files.getContentUri("external")
    }

    object Projection{
        /**
         * ImageModel projections
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
         * VideoModel projections
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
         * AudioModel projections
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
                //" AND "+ MediaStore.Files.FileColumns.DATA +" NOT LIKE ?"+
        ") GROUP BY (" + MediaStore.Files.FileColumns.PARENT
    }

    object Intent{
        const val ACTIVITY_LIB_MAIN = 201

    }

    object Fragment{
        const val IMAGE_VIDEO_FOLDER = 101
        const val AUDIO_FOLDER = 102

        const val IMAGE_LIST = 103
        const val VIDEO_LIST = 104
        const val AUDIO_LIST = 105
    }
}